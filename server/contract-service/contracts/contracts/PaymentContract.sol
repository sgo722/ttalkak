// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

interface IERC20 {
    function transferFrom(address sender, address recipient, uint256 amount) external returns (bool);
}

contract PaymentContract {
    struct Payment {
        address from;
        address to;
        uint256 amount;
        uint256 timestamp;
    }

    address public owner;
    uint256 public ownerFeePercent;
    IERC20 public tokenContract; // ERC20 토큰 컨트랙트

    Payment[] public paymentHistory;

    event PaymentMade(address indexed from, address indexed to, uint256 amount, uint256 ownerFee, uint256 timestamp);

    constructor(uint256 _ownerFeePercent, address _tokenContract) {
        owner = msg.sender;
        ownerFeePercent = _ownerFeePercent;
        tokenContract = IERC20(_tokenContract); // 토큰 컨트랙트 설정
    }

    function sendPayment(address _to, uint256 _amount) external {
        require(_amount > 0, "Amount must be greater than zero");

        uint256 ownerFee = (_amount * ownerFeePercent) / 100;
        uint256 recipientAmount = _amount - ownerFee;

        // ERC20 토큰 전송
        require(tokenContract.transferFrom(msg.sender, _to, recipientAmount), "Token transfer failed");
        require(tokenContract.transferFrom(msg.sender, owner, ownerFee), "Fee transfer failed");

        paymentHistory.push(Payment({
            from: msg.sender,
            to: _to,
            amount: recipientAmount,
            timestamp: block.timestamp
        }));

        emit PaymentMade(msg.sender, _to, recipientAmount, ownerFee, block.timestamp);
    }

    function getPaymentHistory() external view returns (Payment[] memory) {
        return paymentHistory;
    }
}