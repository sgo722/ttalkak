// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";

contract SourceCodeNFT is ERC721 {
    uint256 public currentTokenId = 0;

    struct SourceCodeMetadata {
        string name;
        string commit;
        string version;
        string[] fileHashes;
        uint256 timestamp;
    }

    mapping(uint256 => SourceCodeMetadata) public metadataMapping;

    constructor() ERC721("SourceCodeNFT", "TNF") {}

    function mintSourceCodeNFT(
        string memory name,
        string memory commit,
        string memory version,
        string[] memory fileHashes
    ) public returns (uint256) {
        currentTokenId += 1;
        metadataMapping[currentTokenId] = SourceCodeMetadata({
            name: name,
            commit: commit,
            version: version,
            fileHashes: fileHashes,
            timestamp: block.timestamp
        });
        _mint(msg.sender, currentTokenId);
        return currentTokenId;
    }

    function getMetadata(uint256 tokenId)
        public
        view
        returns (SourceCodeMetadata memory)
    {
        return metadataMapping[tokenId];
    }
}