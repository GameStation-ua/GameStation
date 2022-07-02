const socket = new WebSocket('ws://localhost:8443/notifications', localStorage.getItem('token'));
export default socket