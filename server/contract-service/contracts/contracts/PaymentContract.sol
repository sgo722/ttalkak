// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

interface IERC20 {
    function transferFrom(address sender, address recipient, uint256 amount) external returns (bool);
    function balanceOf(address account) external view returns (uint256);
    function allowance(address owner, address spender) external view returns (uint256); // Allowance 함수 추가
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
    IERC20 public tokenContract; // ERC20 token contract

    Payment[] public paymentHistory;

    event PaymentMade(address indexed from, address indexed to, uint256 amount, uint256 ownerFee, uint256 timestamp);
    event BalanceChecked(address indexed user, uint256 balance);
    event AllowanceChecked(address indexed owner, address indexed spender, uint256 allowance);

    constructor(uint256 _ownerFeePercent, address _tokenContract) {
        owner = msg.sender;
        ownerFeePercent = _ownerFeePercent;
        tokenContract = IERC20(_tokenContract); // Set the token contract
    }

    function sendPayment(address _to, uint256 _amount) external {
        require(_amount > 0, "Amount must be greater than zero");

        uint256 ownerFee = (_amount * ownerFeePercent) / 100;
        uint256 recipientAmount = _amount - ownerFee;

        // Transfer ERC20 tokens
        tokenContract.transferFrom(msg.sender, _to, recipientAmount);
        // tokenContract.transferFrom(msg.sender, owner, ownerFee);

        // paymentHistory.push(Payment({
        //     from: msg.sender,
        //     to: _to,
        //     amount: recipientAmount,
        //     timestamp: block.timestamp
        // }));

        // emit PaymentMade(msg.sender, _to, recipientAmount, ownerFee, block.timestamp);
    }

    function balance(address _account) external view returns (uint256) {
        return tokenContract.balanceOf(_account);
    }

    function checkAllowance(address _owner, address _spender) external view returns (uint256) {
        uint256 allowedAmount = tokenContract.allowance(_owner, _spender);
        return allowedAmount;
    }

    function getPaymentHistory() external view returns (Payment[] memory) {
        return paymentHistory;
    }
}
