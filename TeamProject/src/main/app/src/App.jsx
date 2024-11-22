import Home from "./components/Home.jsx";
import { Route, createBrowserRouter, createRoutesFromElements, RouterProvider } from 'react-router-dom';
import Login from "./components/Login.jsx";
import Dashboard from "./components/Dashboard.jsx";
import OAuthCallback from "./components/OAuthCallback.jsx";
function App() {

    const router = createBrowserRouter(
        createRoutesFromElements(
            <Route>
                <Route index element={<Home />} />
                <Route path="login" element={<Login />} />
                <Route path="/oauth-callback" element={<OAuthCallback />} />
                <Route path="dashboard" element={<Dashboard />} />
            </Route>
                // <Route path="register" element={<Register />} />
        )
    )

  return (
      <>
          <RouterProvider router={router}/>
      </>
  )
}

export default App
