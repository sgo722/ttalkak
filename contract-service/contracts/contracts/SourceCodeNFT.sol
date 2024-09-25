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

    // Mapping from token ID to SourceCodeMetadata
    mapping(uint256 => SourceCodeMetadata) public metadataMapping;

    // Variable to keep track of the next token ID
    uint256 private _nextTokenId;

    constructor() ERC721("SourceCodeToken", "SCT") {}

    // Mint a new NFT representing a source code
    function mintSourceCodeToken(
        string memory name,
        string memory commit,
        string memory version,
        string[] memory fileHashes,
        string memory tokenURI
    ) public returns (uint256) {
        // Assign the next token ID
        uint256 newTokenId = _nextTokenId;

        // Store metadata for the token
        metadataMapping[newTokenId] = SourceCodeMetadata({
            name: name,
            commit: commit,
            version: version,
            fileHashes: fileHashes,
            timestamp: block.timestamp
        });

        // Mint the NFT to the caller
        _safeMint(msg.sender, newTokenId);

        // Set the token URI (typically used for metadata like image, description, etc.)
        _setTokenURI(newTokenId, tokenURI);

        // Increment the next token ID for future mints
        _nextTokenId++;

        return newTokenId;
    }

    // Retrieve metadata for a specific token ID
    function getMetadata(uint256 tokenId)
        public
        view
        returns (SourceCodeMetadata memory)
    {
        return metadataMapping[tokenId];
    }
}
