import {FormControl} from "@mui/base";
import {Button, TextField} from "@mui/material";
import {useState} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";

export default function Setup() {
    const [clientName, setClientName] = useState("");
    const [location, setLocation] = useState("");
    const [error, setError] = useState({ clientName: false, location: false });

    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid']);

    const navigate = useNavigate();


    const handleSubmit = () => {
        let hasError = false;

        if (!clientName) {
            setError((prev) => ({ ...prev, clientName: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, clientName: false }));
        }

        if (!location) {
            setError((prev) => ({ ...prev, location: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, location: false }));
        }

        if (hasError) return;

        const clientId = Number(cookies.uid) % (2 ** 31);
        console.log('new client id', clientId);
        const newClient = {
            id: clientId,
            name: `${clientName}`,
            location: `${location}`
        };

        return fetch('https://restinginbed.ue.r.appspot.com/createClient', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newClient),
        }).then(response => {
            if (response.ok) {
                navigate('/')
            } else {
                navigate('/logout')
            }
        })
    };

    const handleClientName = (e) => {
        e.preventDefault();
        setClientName(e.target.value);
    };

    const handleLocation = (e) => {
        e.preventDefault();
        setLocation(e.target.value);
    };

    return (
        <div className="w-screen h-screen flex items-center justify-center">
            <form action="" className="flex flex-col gap-4 w-1/5">
                <h1 className="text-2xl">Register</h1>
                <FormControl className="flex flex-col gap-4">
                    <TextField
                        required
                        fullWidth
                        label="Name"
                        onChange={(e) => handleClientName(e)}
                        value={clientName}
                        error={error.clientName}
                        helperText={error.clientName ? "Name is required" : ""}
                    />
                    <TextField
                        required
                        fullWidth
                        label="Location"
                        onChange={(e) => handleLocation(e)}
                        value={location}
                        error={error.location}
                        helperText={error.location ? "Location is required" : ""}
                    />
                    <Button variant="contained" onClick={handleSubmit}>
                        Submit
                    </Button>
                </FormControl>
            </form>
        </div>
    );
}
