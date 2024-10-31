// /types/tokenDTO.ts
export interface TokenDTO {
  accessToken: string;         // JWT token
  tokenType: string;            // For example, "Bearer"
  accessTokenExpiresIn: number; // Token expiration time in seconds
}
