/**
 * 주어진 일수만큼 이전의 날짜를 ISO 형식으로 반환하는 함수
 * @param daysAgo 과거 몇 일 전인지 (0이면 오늘)
 * @returns ISO 형식의 날짜 문자열
 */

export const getISODate = (daysAgo: number = 0): string => {
  const date = new Date();
  date.setDate(date.getDate() - daysAgo);
  return date.toISOString();
};
