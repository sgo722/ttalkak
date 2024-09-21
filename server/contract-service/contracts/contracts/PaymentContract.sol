// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract PaymentContract {
    struct Payment {
        address from;
        address to;
        uint256 amount;
        uint256 timestamp;
    }

    address public owner;
    uint256 public ownerFeePercent;

    Payment[] public paymentHistory;

    event PaymentMade(address indexed from, address indexed to, uint256 amount, uint256 ownerFee, uint256 timestamp);

    constructor(uint256 _ownerFeePercent) {
        owner = msg.sender;
        ownerFeePercent = _ownerFeePercent;
    }

    function sendPayment(address payable _to) external payable {
        require(msg.value > 0, "Amount must be greater than zero");

        uint256 ownerFee = (msg.value * ownerFeePercent) / 100;

        uint256 recipientAmount = msg.value - ownerFee;

        _to.transfer(recipientAmount);
        payable(owner).transfer(ownerFee);

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

    function getPaymentCount() external view returns (uint256) {
        return paymentHistory.length;
    }

    function getPaymentByIndex(uint256 index) external view returns (address from, address to, uint256 amount, uint256 timestamp) {
        require(index < paymentHistory.length, "Invalid index");

        Payment memory payment = paymentHistory[index];
        return (payment.from, payment.to, payment.amount, payment.timestamp);
    }
}