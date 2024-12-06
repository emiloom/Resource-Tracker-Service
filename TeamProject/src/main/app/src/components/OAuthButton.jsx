import { useNavigate } from "react-router-dom";
import { FcGoogle } from "react-icons/fc";
import {useCookies} from "react-cookie";
import {getType} from "./utils.js";

const OAuthButton = () => {
    const navigate = useNavigate();
    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid', 'type']);

    const handleOAuthSubmit = () => {
        const googleClientId = "667083991765-8egqcnldoa0m80c7kpm1q4korlbmvn91.apps.googleusercontent.com";
        const callbackUrl = `${window.location.origin}/oauth-callback`;
        const targetUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${googleClientId}&redirect_uri=${encodeURIComponent(callbackUrl)}&response_type=token&scope=openid%20email%20profile`;

        const popup = window.open(
            targetUrl,
            "popup",
            "popup=true,height=700,width=500"
        );

        const handleMessage = async (event) => {
            if (event.origin !== window.location.origin) {
                console.error("Untrusted origin:", event.origin);
                return;
            }

            const data = event.data;
            if (data.accessToken) {
                setCookies('auth_token', data.accessToken);
                setCookies('exp_time', data.expiresIn);

                // get uid with auth_token
                fetch('https://restinginbed.ue.r.appspot.com/api/resource', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${data.accessToken}`,
                    },
                })
                    .then(response => response.json())
                    .then(data =>{
                        const userId = data['sub'];
                        setCookies('uid', userId);

                        const clientId = Number(userId) % (2 ** 31);

                        getType(clientId)
                            .then(type => {
                                if (type === 'client') {
                                    setCookies('type', 'client');
                                    navigate('/')
                                } else if (type === 'organization') {
                                    setCookies('type', 'organization');
                                    navigate('/dashboard')
                                } else {
                                    navigate('/choice')
                                }
                            })


                    })
                    .catch(error => console.error('Error:', error));
            }
        };

        window.addEventListener("message", handleMessage);

        const timer = setInterval(() => {
            if (popup.closed) {
                clearInterval(timer);
                window.removeEventListener("message", handleMessage);
            }
        }, 500);
    };

    return (
        <button
            onClick={handleOAuthSubmit}
            className="flex items-center gap-4 rounded-full p-5 border-2 border-black"
        >
            <FcGoogle className="w-8 h-8"/>
            <p>Sign in with Google</p>
        </button>
    );
};

export default OAuthButton;
