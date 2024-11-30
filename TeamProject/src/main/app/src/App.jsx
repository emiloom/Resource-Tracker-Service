import Home from "./components/Home.jsx";
import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider } from 'react-router-dom';
import Login from "./components/Login.jsx";
import Dashboard from "./components/Dashboard.jsx";
import OAuthCallback from "./components/OAuthCallback.jsx";
import {CookiesProvider} from "react-cookie";
import {Logout} from "@mui/icons-material";
import Setup from "./components/Setup.jsx";
function App() {

    const router = createBrowserRouter(
        createRoutesFromElements(
            <Route>
                <Route index element={<Home />} />
                <Route path="login" element={<Login />} />
                <Route path="oauth-callback" element={<OAuthCallback />} />
                <Route path="dashboard" element={<Dashboard />} />
                <Route path="logout" element={<Logout />} />
                <Route path="setup" element={<Setup />} />
            </Route>
        )
    )

  return (
      <div className="w-screen h-screen">
          <CookiesProvider defaultSetOptions={{path: '/'}}>
              <RouterProvider router={router}/>
          </CookiesProvider>
      </div>
  )
}

export default App
