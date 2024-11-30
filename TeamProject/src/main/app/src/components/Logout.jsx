import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useCookies} from "react-cookie";


export default function Logout() {

    const navigate = useNavigate();
    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid']);

    const handleLogout = () => {
        setCookies("auth_token", null);
        setCookies("exp_time", null);
        setCookies("uid", null);
        navigate("/login");
    }

    return (
        <Button
            onClick={handleLogout}
            variant="contained"
        >
            Logout
        </Button>
    )
}