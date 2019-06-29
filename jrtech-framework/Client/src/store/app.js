export default {
  state: {
    user: {
      loginToken: window.localStorage.getItem('loginToken'),
      userId: window.localStorage.getItem('userId'),
      userLoginId: window.localStorage.getItem('userLoginId'),
      userDisplayName: window.localStorage.getItem('userDisplayName'),
      userMobile: window.localStorage.getItem('userMobile'),
      userPermissions: null,
    },
  },
  getters: {
    user: state => state.user,
  },
  mutations: {
    loginIn(state, loginUser) {
      window.localStorage.setItem('loginToken', loginUser.appKey)
      window.localStorage.setItem('userId', loginUser.userId)
      window.localStorage.setItem('userLoginId', loginUser.userLoginId)
      window.localStorage.setItem('userDisplayName', loginUser.userDisplayName)
      window.localStorage.setItem('userMobile', loginUser.userMobile)

      state.user.loginToken = loginUser.appKey
      state.user.userId = loginUser.userId
      state.user.userLoginId = loginUser.userLoginId
      state.user.userDisplayName = loginUser.userDisplayName
      state.user.userMobile = loginUser.userMobile
    },
    loginOut(state) {
      window.localStorage.removeItem('loginToken')
      window.localStorage.removeItem('userId')
      window.localStorage.removeItem('userLoginId')
      window.localStorage.removeItem('userDisplayName')
      window.localStorage.removeItem('userMobile')

      state.user.loginToken = ''
      state.user.userId = ''
      state.user.userLoginId  = ''
      state.user.userDisplayName = ''
      state.user.userMobile = ''
    },
    profile(state, userInfo) {
      window.localStorage.setItem('userId', userInfo.userId)
        window.localStorage.setItem('userLoginId', userInfo.userLoginId)
      window.localStorage.setItem('userDisplayName', userInfo.userDisplayName)
      window.localStorage.setItem('userMobile', userInfo.userMobile)
      state.user.userId = userInfo.userId
      state.user.userLoginId = userInfo.userLoginId
      state.user.userDisplayName = userInfo.userDisplayName
      state.user.userMobile = userInfo.userMobile
    },
    setPermissions(state,permissions){
      state.user.userPermissions = permissions;
    }
  }
}
