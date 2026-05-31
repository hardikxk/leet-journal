const rawGatewayBaseUrl = import.meta.env.PUBLIC_GATEWAY_BASE_URL ?? "http://localhost:9000";

export const gatewayBaseUrl = rawGatewayBaseUrl.replace(/\/$/, "");
export const oauthAuthorizationUrl = `${gatewayBaseUrl}/oauth2/authorization/proxy-client-oidc`;