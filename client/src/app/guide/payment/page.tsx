import { FaExclamationTriangle } from "react-icons/fa";
import Image from "next/image";

export default function PaymentGuidePage() {
  const container = "px-4 py-10 border-t bg-white";
  const containerTitle = "text-2xl font-bold mb-4";

  return (
    <div className="container px-8 min-h-screen max-h-screen">
      <div className="flex-grow px-4 sm:px-6 lg:px-8 pt-16 grid">
        <div className="flex flex-col justify-center h-40 mb-6">
          <h2 className="text-4xl font-bold mb-4 text-center">결제 가이드</h2>
          <div className="text-center mt-2 mb-4 text-gray-600">
            딸깍 서비스에서 결제 기능을 이용하기 위해 필요한 설정 과정을
            안내합니다.
          </div>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>1. MetaMask 설치 및 회원가입</h2>
          <p className="text-gray-700 mb-4">
            MetaMask는 블록체인 지갑 애플리케이션으로, Chrome, Firefox 등의
            브라우저 확장 프로그램으로 설치할 수 있습니다.
          </p>
          <ul className="list-disc list-inside space-y-2">
            <li>
              <a
                href="https://metamask.io/"
                target="_blank"
                className="text-blue-500 underline hover:text-blue-700"
              >
                MetaMask 공식 사이트
              </a>
              에 접속하여 <strong>&quot;다운로드&quot;</strong> 버튼을 클릭한
              뒤, 브라우저에 MetaMask를 추가합니다.
            </li>
            <li>
              MetaMask 확장 프로그램을 설치한 후, 계정을 생성하고 로그인합니다.
            </li>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>
            2. MetaMask 주소 및 Private Key 확인
          </h2>
          <p className="text-gray-700 mb-4">
            로그인 후, 결제에 필요한 <strong>지갑 주소</strong>와{" "}
            <strong>Private Key</strong>를 확인해야 합니다.
          </p>
          <ul className="list-disc list-inside space-y-2">
            <li>
              MetaMask를 실행하고, 상단의 계정 이름을 클릭하여{" "}
              <strong>&quot;계정 세부 정보(Account Details)&quot;</strong>{" "}
              메뉴를 선택합니다.
            </li>
            <li>
              <strong>지갑 주소</strong>는 &quot;Account&quot; 아래에 표시된 긴
              문자열(예:{" "}
              <code className="bg-gray-100 px-2 py-1 rounded text-sm">
                0x1234...
              </code>
              )입니다. 이 주소를 복사하여 마이페이지의{" "}
              <strong>지갑 주소 등록하기</strong> 입력란에 붙여넣습니다.
            </li>
            <li>
              <strong>Private Key</strong>를 확인하려면{" "}
              <strong>&quot;계정 세부 정보&quot;</strong> 화면에서{" "}
              <strong>&quot;키 내보내기(Export Private Key)&quot;</strong>를
              선택합니다.
            </li>
          </ul>
          <div className="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4 my-4">
            <div className="flex items-center">
              <FaExclamationTriangle className="mr-2" />
              <span className="font-bold">주의!</span>
            </div>
            <p>
              Private Key는 외부에 노출되지 않도록 주의해야 합니다. 잘못된 곳에
              입력하면 지갑 내 자산이 도용될 수 있습니다.
            </p>
          </div>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>3. MetaMask 네트워크 등록</h2>
          <p className="text-gray-700 mb-4">
            MetaMask에 SSAFY 네트워크를 등록해야 합니다. 네트워크 등록을 통해
            블록체인과 연결하고,
            <br /> 결제 및 서비스에 필요한 권한 설정을 할 수 있습니다. 아래
            정보를 참고하여 네트워크를 등록하세요.
          </p>
          <Image
            src="/network.png"
            alt="MetaMask 네트워크 등록 이미지"
            width={280}
            height={400}
            className="border rounded mb-6"
          />
          <ul className="list-disc list-inside space-y-2">
            <li>
              MetaMask를 실행하고, 우측 상단의{" "}
              <strong>&quot;네트워크 선택&quot;</strong> 버튼을 클릭하여{" "}
              <strong>&quot;네트워크 추가(Add Network)&quot;</strong> 버튼을
              선택합니다.
            </li>
            <li>네트워크 추가 화면에서 아래 정보를 입력하세요:</li>
            <ul className="list-inside ml-6 mt-2">
              <li>
                <strong>네트워크 이름:</strong> SSAFY
              </li>
              <li>
                <strong>새 RPC URL:</strong> https://rpc.ssafy-blockchain.com
              </li>
              <li>
                <strong>체인 ID:</strong> 31221
              </li>
              <li>
                <strong>통화 기호:</strong> ETH
              </li>
              <li>
                <strong>블록 탐색기 URL(옵션):</strong> 비워두기
              </li>
            </ul>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>4. 지갑 등록 및 권한 설정하기</h2>
          <p className="text-gray-700 mb-4">
            웹사이트의 마이페이지에서 지갑 주소{" "}
            <strong>&quot;등록하기&quot;</strong> 버튼을 눌러 등록창을 열 수
            있습니다. <br />
            지갑 주소와 Private Key를 입력한 후 <strong>다음</strong> 버튼을
            눌러 <strong>권한 설정</strong> 단계로 이동합니다.
          </p>
          <ul className="list-disc list-inside space-y-2">
            <li>
              권한 설정 단계에서는 사용자의 MetaMask 지갑에서 권한 요청 팝업이
              뜹니다.
            </li>
            <li>
              <strong>&quot;승인(Approve)&quot;</strong> 버튼을 클릭하여 권한을
              부여합니다.
            </li>
            <li>
              권한 설정이 완료되면, <strong>지갑 등록</strong>이 완료되었다는
              메시지를 확인할 수 있습니다.
            </li>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>5. 결제 방식 및 요금 안내</h2>
          <p className="text-gray-700 mb-4">
            딸깍 서비스에서는 <strong>15분마다 10코인</strong>이 자동으로
            결제됩니다. 결제 과정은 다음과 같습니다:
          </p>
          <ul className="list-disc list-inside space-y-2">
            <li>
              <strong>자동 결제:</strong> 사용자가 서비스를 이용하는 동안,
              15분마다 10ssf(SsafyCoin)만큼 자동으로 차감됩니다.
            </li>
            <li>
              <strong>결제 확인:</strong> 결제 내역은 마이페이지의{" "}
              <strong>결제 내역 확인</strong> 섹션에서 확인할 수 있습니다.
            </li>
            <li>
              <strong>잔액 부족 시:</strong> 잔액이 부족할 경우, 서비스 사용이
              일시 중지되며 추가 충전이 필요합니다.
            </li>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>6. 주의 사항</h2>
          <p className="text-gray-700 mb-4">
            권한 설정이 완료되지 않을 경우, 배포한 서비스가 15분 후 자동으로
            종료됩니다.
            <br /> 서비스 운영을 위해 권한 설정 절차를 반드시 완료해주세요.
          </p>
        </div>
        <div className="h-20"></div>
      </div>
    </div>
  );
}
