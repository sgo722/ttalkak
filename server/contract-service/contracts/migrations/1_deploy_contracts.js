const PaymentContract = artifacts.require("PaymentContract");

module.exports = function (deployer) {
  const ownerFeePercent = 30;
  deployer.deploy(PaymentContract, ownerFeePercent);
};