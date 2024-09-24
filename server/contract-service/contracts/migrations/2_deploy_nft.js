const SourceCodeToken = artifacts.require("SourceCodeToken");

module.exports = function (deployer) {
    deployer.deploy(SourceCodeToken);
};