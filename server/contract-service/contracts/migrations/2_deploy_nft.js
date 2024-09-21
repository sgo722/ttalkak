const SourceCodeNFT = artifacts.require("SourceCodeNFT");

module.exports = function (deployer) {
    deployer.deploy(SourceCodeNFT);
};