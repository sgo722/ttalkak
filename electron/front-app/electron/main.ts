import { app, BrowserWindow, Menu, Tray, shell, ipcMain } from "electron";
import { fileURLToPath } from "node:url";
import path from "node:path";
import * as os from "os";
import { githubDownLoadAndUnzip } from "./managers/githubManager";
import { handleDatabaseSetup } from "./managers/dockerDBManager";
import { handleFetchContainerLogs } from "./managers/dockerLogsManager";
import {
  handleGetDockerEvent,
  handleGetContainerMemoryUsage,
  handleGetContainerStatsPeriodic,
} from "./managers/dockerEventManager";
import {
  handlecheckDockerStatus,
  getDockerPath,
  handleStartDocker,
  handleGetDockerImageList,
  handleFetchDockerImages,
  handleGetDockerContainerList,
  handleFetchDockerContainer,
  handleBuildDockerImage,
  registerContainerIpcHandlers,
  handleFindDockerFile,
} from "./managers/dockerManager";
import { powerSaveBlocker } from "electron";
import { setMainWindow, registerPgrokIpcHandlers } from "./pgrokManager";
import { stopAllPgrokProcesses } from "./pgrokManager";

const __dirname = path.dirname(fileURLToPath(import.meta.url));

process.env.APP_ROOT = path.join(__dirname, "..");

export const VITE_DEV_SERVER_URL = process.env["VITE_DEV_SERVER_URL"];
export const MAIN_DIST = path.join(process.env.APP_ROOT, "dist-electron");
export const RENDERER_DIST = path.join(process.env.APP_ROOT, "dist");

process.env.VITE_PUBLIC = VITE_DEV_SERVER_URL
  ? path.join(process.env.APP_ROOT, "public")
  : RENDERER_DIST;

let win: BrowserWindow | null = null;
// let loadingWindow: BrowserWindow | null = null;
let tray: Tray | null = null;
let isQuiting = false; // 애플리케이션 종료 상태를 추적하는 변수

//DockerManager IPC handler 등록
function registerIpcHandlers() {
  handlecheckDockerStatus(); // Docker 상태 체크 핸들러 초기화
  getDockerPath(); // Docker 경로 핸들러 초기화
  handleStartDocker(); // Docker 데스크탑 시작 핸들러 초기화
  handleDatabaseSetup(); //db용이미지pull 및 실행
  handleGetDockerEvent(); // Docker 이벤트 핸들러 초기화
  handleGetDockerImageList(); // Docker 이미지 목록 핸들러 초기화
  handleFetchDockerImages(); // Docker 단일 이미지 핸들러 초기화
  handleGetDockerContainerList(); // Docker 컨테이너 목록 핸들러 초기화
  handleFetchDockerContainer(); // Docker 단일 컨테이너 핸들러 초기화
  handleFetchContainerLogs(); // Docker 컨테이너 로그 핸들러 초기화
  handleBuildDockerImage(); // Docker 이미지 빌드 핸들러 초기화
  handleFindDockerFile(); //도커 파일 위치 경로 찾기
  githubDownLoadAndUnzip(); //깃허브 파일 다운 및 압축해제
  handleGetContainerMemoryUsage(); //컨테이너별 메모리 사용량
  registerContainerIpcHandlers(); //컨테이너 생성, 실행, 정지, 삭제
  handleGetContainerStatsPeriodic(); //컨테이너별 stats 주기적 체크
  // pgrok 관련 IPC 핸들러 등록
  registerPgrokIpcHandlers();

  ipcMain.handle("get-cpu-usage", async () => {
    try {
      const cpuUsage = calculateCpuUsage();
      return cpuUsage;
    } catch (error) {
      console.error("Failed to get CPU usage:", error);
      throw error;
    }
  });
}
//OS 종류 확인 후 전달
ipcMain.handle("get-os-type", async () => {
  const platform = os.platform();

  switch (platform) {
    case "win32":
      return "WINDOWS";
    case "darwin":
      return "MACOS";
    case "linux":
      return "LINUX";
    default:
      return "Unknown";
  }
});

function calculateCpuUsage() {
  const cpus = os.cpus();
  let user = 0,
    nice = 0,
    sys = 0,
    idle = 0,
    irq = 0,
    total = 0;

  for (const cpu of cpus) {
    user += cpu.times.user;
    nice += cpu.times.nice;
    sys += cpu.times.sys;
    idle += cpu.times.idle;
    irq += cpu.times.irq;
  }

  total = user + nice + sys + idle + irq;
  const cpuUsage = ((total - idle) / total) * 100;

  return parseFloat(cpuUsage.toFixed(2));
}

// //
// // 로딩 창 생성
// function createLoadingWindow() {
//   loadingWindow = new BrowserWindow({
//     width: 300,
//     height: 300,
//     frame: false, // 창 프레임 제거
//     transparent: true, // 투명 배경 설정
//     alwaysOnTop: true,
//     resizable: false,
//     show: false, // ready-to-show에서 표시되도록 설정
//   });

//   loadingWindow.loadFile(path.join(__dirname, "loading.html"));

//   loadingWindow.once("ready-to-show", () => {
//     loadingWindow?.show();
//   });
// }

// 새로운 Electron 창 오픈
async function createWindow() {
  win = new BrowserWindow({
    frame: false,
    titleBarStyle: "hidden",
    minWidth: 1024,
    minHeight: 400,
    height: 650,
    icon: path.join(process.env.VITE_PUBLIC, "favicon.png"),
    webPreferences: {
      preload: path.join(__dirname, "preload.mjs"),
      contextIsolation: true,
      nodeIntegration: true,
      webSecurity: true,
      nodeIntegrationInWorker: true,
      backgroundThrottling: false, // 백그라운드에서 앱이 멈추지 않도록 설정
    },
    autoHideMenuBar: true,
  });

  if (VITE_DEV_SERVER_URL) {
    await win.loadURL(VITE_DEV_SERVER_URL);
  } else {
    win.loadFile(path.join(RENDERER_DIST, "index.html"));
  }

  // 메인 창이 준비되면 로딩창 닫기
  // win.once("ready-to-show", () => {
  //   if (loadingWindow) {
  //     loadingWindow.close(); // 메인 창이 준비되면 로딩창 닫기
  //     loadingWindow = null;
  //   }
  //   win?.show(); // 메인 창 표시
  // });

  setMainWindow(win); // pgrokManager에 메인 윈도우 설정

  win.on("close", (event) => {
    if (!isQuiting) {
      event.preventDefault();
      win?.hide();
    }
  });

  win.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url);
    return { action: "deny" };
  });

  // CustomBar IPC 핸들러
  ipcMain.on("minimize-window", () => {
    win?.minimize();
  });

  ipcMain.on("maximize-window", () => {
    if (win?.isMaximized()) {
      win.unmaximize();
    } else {
      win?.maximize();
    }
  });

  ipcMain.on("close-window", () => {
    win?.close();
  });
}

// 애플리케이션 종료 전 실행할 함수들
app.on("before-quit", async (event) => {
  event.preventDefault();

  try {
    await stopAllPgrokProcesses(); // 실행 중인 모든 pgrok 프로세스 종료
    app.quit(); // 모든 프로세스가 종료된 후 애플리케이션 종료
  } catch (error) {
    console.error("Failed to stop pgrok processes:", error);
    app.quit(); // 에러가 발생해도 애플리케이션 종료
  }
});

// Create the system tray icon and menu
function createTray() {
  tray = new Tray(path.join(process.env.VITE_PUBLIC, "favicon.png"));

  const contextMenu = Menu.buildFromTemplate([
    {
      label: "Show App",
      click: () => {
        win?.show();
      },
    },
    {
      label: "Quit",
      click: () => {
        isQuiting = true;
        app.quit();
      },
    },
  ]);

  tray.setToolTip("My Electron App");
  tray.setContextMenu(contextMenu);

  tray.on("click", () => {
    win?.show();
  });
}

// powerSaveBlocker 시작 함수
function startPowerSaveBlocker() {
  const id = powerSaveBlocker.start("prevent-app-suspension");
  console.log(`PowerSaveBlocker started with id: ${id}`);
}

app
  .whenReady()
  // .then(createLoadingWindow)
  .then(registerIpcHandlers) // IPC 핸들러 등록
  .then(createWindow) // 윈도우 생성
  .then(createTray) // 트레이 생성
  .then(startPowerSaveBlocker)
  .catch((error) => {
    console.error("Failed to start application:", error);
  });

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

app.on("activate", async () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    await createWindow();
  }
});
