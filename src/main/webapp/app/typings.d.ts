declare const VERSION: string;
declare const SERVER_API_URL: string;
declare const DEVELOPMENT: string;
declare const I18N_HASH: string;

declare module '*.json' {
  const value: any;
  export default value;
}

declare module '@comment/entities-routes' {
  const _default: () => JSX.Element;
  export default _default;
}

declare module '@comment/entities-menu' {
  const _default: () => JSX.Element;
  export default _default;
}
