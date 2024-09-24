export const getISODate = (daysAgo: number = 0): string => {
  const date = new Date();
  date.setDate(date.getDate() - daysAgo);
  date.setHours(0, 0, 0, 0);

  const koreaTimeInMs = date.getTime() + 9 * 60 * 60 * 1000;
  const koreaDate = new Date(koreaTimeInMs);

  return koreaDate.toISOString();
};

export const getWeekStartDate = (): string => {
  const date = new Date();
  const day = date.getDay(); // 0 (일요일) ~ 6 (토요일)
  const diff = date.getDate() - day; // 해당 주의 일요일로 이동
  const sunday = new Date(date.setDate(diff));

  sunday.setHours(0, 0, 0, 0);

  const koreaTimeInMs = sunday.getTime() + 9 * 60 * 60 * 1000;
  const koreaSunday = new Date(koreaTimeInMs);

  return koreaSunday.toISOString();
};

export const formatTimestamp = (isoString: string): string => {
  const date = new Date(isoString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};
