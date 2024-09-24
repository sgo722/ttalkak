const PaymentContract = artifacts.require("PaymentContract");

module.exports = function (deployer) {
  const ownerFeePercent = 30;
  deployer.deploy(PaymentContract, ownerFeePercent, "0x066b74Fc73bfaf0C266b0269F91dDeeB5aAB6998");
};