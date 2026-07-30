import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from 'react-router-dom'
import './App.css'
import Box from '@mui/material/Box';
import { Button } from '@mui/material'
import { Activity, useContext, useEffect, useState } from 'react'
import { useDispatch } from 'react-redux'
import { setCredentials } from './store/authSlice'
import { AuthContext } from 'react-oauth2-code-pkce'
import ActivityForm from './components/ActivityForm';
import ActivityList from './components/ActivityList';
import ActivityDetail from './components/ActivityDetail';

const ActivityPage = () => {
  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm />
      <ActivityList />
    </Box>
  )
}

function App() {
  const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext)
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);
  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);
  return (
    <Router>
      {
        !token ? (
          <Button variant='containesd' color='#dc004e' onClick={() => { logIn() }}> Login</Button >
        ) : (
          <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
            <Routes>
              <Route path='/activities' element={<ActivityPage />} />
              <Route path='/activities/:id' element={<ActivityDetail />} />
              <Route path='/' element={token ? <Navigate to='/activities' replace /> : <div>welcome,please login!</div>} />
            </Routes>
          </Box>
        )

      }
    </Router >
  )
}

export default App
