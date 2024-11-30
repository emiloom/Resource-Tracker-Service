import {FormControl} from "@mui/base";
import {Button, TextField} from "@mui/material";
import {useState} from "react";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";

export default function Setup() {
    const [orgName, setOrgName] = useState("");
    const [location, setLocation] = useState("");
    const [error, setError] = useState({ orgName: false, location: false });

    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid']);

    const navigate = useNavigate();


    const handleSubmit = () => {
        let hasError = false;

        if (!orgName) {
            setError((prev) => ({ ...prev, orgName: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, orgName: false }));
        }

        if (!location) {
            setError((prev) => ({ ...prev, location: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, location: false }));
        }

        if (hasError) return;

        const organizationId = Number(cookies.uid) % (2 ** 31);
        console.log('new org id', organizationId);
        const newOrganization = {
            id: organizationId,
            name: `${orgName}`,
            location: `${location}`
        };

        return fetch('http://localhost:8080/createOrganization', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newOrganization),
        }).then(response => {
            if (response.ok) {
                navigate('/dashboard')
            } else {
                navigate('/logout')
            }
        })
    };

    const handleOrgName = (e) => {
        e.preventDefault();
        setOrgName(e.target.value);
    };

    const handleLocation = (e) => {
        e.preventDefault();
        setLocation(e.target.value);
    };

    return (
        <div className="w-screen h-screen flex items-center justify-center">
            <form action="" className="flex flex-col gap-4 w-1/5">
                <h1 className="text-2xl">Register your Organization</h1>
                <FormControl className="flex flex-col gap-4">
                    <TextField
                        required
                        fullWidth
                        label="Organization Name"
                        onChange={(e) => handleOrgName(e)}
                        value={orgName}
                        error={error.orgName}
                        helperText={error.orgName ? "Organization Name is required" : ""}
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
