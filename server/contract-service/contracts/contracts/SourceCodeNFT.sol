// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";

contract SourceCodeToken is ERC721URIStorage {
    struct SourceCodeMetadata {
        string name;
        string commit;
        string version;
        string[] fileHashes;
        uint256 timestamp;
    }

    uint256 private _nextTokenId;

    constructor() ERC721("SourceCodeToken", "SCT") {}

    function mintSourceCodeToken(
        string memory tokenURI
    ) public returns (uint256) {
        uint256 newTokenId = ++_nextTokenId;

        _safeMint(msg.sender, newTokenId);

        _setTokenURI(newTokenId, tokenURI);

        

        return newTokenId;
    }
}
