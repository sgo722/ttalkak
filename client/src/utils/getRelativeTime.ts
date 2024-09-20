const getRelativeTime = (dateString: string): string => {
  const now = new Date();
  const past = new Date(dateString);
  const diffInSeconds = Math.floor((now.getTime() - past.getTime()) / 1000);

  if (diffInSeconds < 60) return "방금 전";
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}분 전`;
  if (diffInSeconds < 86400)
    return `${Math.floor(diffInSeconds / 3600)}시간 전`;
  if (diffInSeconds < 604800)
    return `${Math.floor(diffInSeconds / 86400)}일 전`;
  if (diffInSeconds < 2592000)
    return `${Math.floor(diffInSeconds / 604800)}주 전`;
  if (diffInSeconds < 31536000)
    return `${Math.floor(diffInSeconds / 2592000)}달 전`;
  return `${Math.floor(diffInSeconds / 31536000)}년 전`;
};

export default getRelativeTime;
