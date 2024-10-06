import { FallbackProps } from "react-error-boundary";

export interface ErrorFallBackProps {
  error: Error & { digest?: string };
  reset: () => void;
}

export interface ErrorBoundaryFallBackProps extends FallbackProps {
  onClose?: () => void;
}
