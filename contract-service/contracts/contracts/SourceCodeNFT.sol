// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract SourceCodeToken is ERC20 {
    struct SourceCodeMetadata {
        string name;
        string commit;
        string version;
        string[] fileHashes;
        uint256 timestamp;
    }

    uint256 public totalMintedTokens = 0;

    mapping(uint256 => SourceCodeMetadata) public metadataMapping;

    constructor() ERC20("SourceCodeToken", "SCT") {}

    function mintSourceCodeToken(
        string memory name,
        string memory commit,
        string memory version,
        string[] memory fileHashes,
        uint256 amount
    ) public returns (uint256) {
        // Increment the token ID
        totalMintedTokens += 1;

        // Store the metadata associated with this minting
        metadataMapping[totalMintedTokens] = SourceCodeMetadata({
            name: name,
            commit: commit,
            version: version,
            fileHashes: fileHashes,
            timestamp: block.timestamp
        });

        // Mint the specified amount of tokens to the sender
        _mint(msg.sender, amount);

        return totalMintedTokens;
    }

    function getMetadata(uint256 tokenId)
        public
        view
        returns (SourceCodeMetadata memory)
    {
        return metadataMapping[tokenId];
    }
}
