export const authConfig = {
    clientId: 'myClientID',
    authorizationEndpoint: 'https://myAuthProvider.com/auth',
    tokenEndpoint: 'https://myAuthProvider.com/token',
    redirectUri: 'http://localhost:3000/',
    scope: 'someScope openid',
    onRefreshTokenExpire: (event) => event.logIn(),
}