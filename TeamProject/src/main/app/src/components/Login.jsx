import {Button, TextField} from "@mui/material";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function Login () {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(false);

    const navigate = useNavigate();

    const handleChangeEmail = (e) => {
        setEmail(e.target.value);
    }

    const handleChangePassword = (e) => {
        setPassword(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        const validateEmail = (email) => {
            return String(email)
                .toLowerCase()
                .match(
                    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                );
        };

        console.log(validateEmail(email))

        if (!validateEmail(email)) {
            setError(true);
            return
        }

        // check if email and password match
        navigate('/', {state: {authenticated: true}});
    }

    const handleEnter = (e) => {
        if (e.key === "Enter") {
            handleSubmit(e)
        }
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
                <div className="w-1/3 flex flex-col gap-4">
                    <TextField
                        onChange={(e) => handleChangeEmail(e)}
                        label="Email"
                        type="email"
                        variant="outlined"
                    />
                    <TextField
                        onChange={(e) => handleChangePassword(e)}
                        label="Password"
                        type="password"
                        variant="outlined"
                        onKeyDown={handleEnter}
                    />
                </div>
                <Button
                    variant="contained"
                    onClick={(e) => handleSubmit(e)}
                >
                    Log in
                </Button>
            </div>
        </div>
    )
}