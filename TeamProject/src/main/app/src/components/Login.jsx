import {Button, TextField} from "@mui/material";
import {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {Google} from "@mui/icons-material";
import OAuthButton from "./OAuthButton.jsx";

export default function Login () {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleError = () => {
        setError(true);
    }


    return (
        <div
            className="w-screen h-screen flex"
        >
            <div className="w-3/5 bg-blue-400"></div>
            <div
                className="w-2/5 flex flex-col gap-4 items-center justify-center"
            >
                <h1 className="text-3xl">Log in</h1>
                <OAuthButton onError={handleError} />
            </div>
        </div>
    )
}