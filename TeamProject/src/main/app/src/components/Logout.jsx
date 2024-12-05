import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useCookies} from "react-cookie";


export default function Logout() {

    const navigate = useNavigate();
    const [cookies, setCookies, removeCookie] = useCookies(['auth_token', 'exp_time', 'uid']);
    const redirect_to_login = useNavigate();
    
    const clientId = "667083991765-8egqcnldoa0m80c7kpm1q4korlbmvn91.apps.googleusercontent.com";

    const handleLogout = () => {
        removeCookie("auth_token");
        removeCookie("exp_time");
        removeCookie("uid");

        if (cookies.auth_token) {
            fetch(`https://oauth2.googleapis.com/revoke?token=${cookies.auth_token}`, {
                method: 'POST',
                headers: { 'Content-type': `https://restinginbed.ue.r.appspot.com/createClient?clientId=${clientId}` }
            }).catch(err => console.error('Error revoking token:', err));
        }

        redirect_to_login('/login')
    };

    return (
        <Button
            onClick={handleLogout}
            variant="contained"
        >
            Logout
        </Button>
    )
}