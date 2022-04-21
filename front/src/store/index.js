import { createStore } from 'vuex'

export default createStore({
  state: {
    mesage:'',
    tokenUser:'',
    search:'',
    tags: {
      token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoiMTUxNjIzOTAyMiIsImlkIjoiMSJ9.OWIP66rhu8RQrqi0S5ISjwUusDXFP2P2fI0xfuI5EO4",
      tags: [
        {
          tag: "wasd"
        },
        {
          tag: "ert"
        },
        {
          tag: "qwerty"
        }
      ]
    }
  }
})
