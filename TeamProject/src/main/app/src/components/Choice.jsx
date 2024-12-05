import {useNavigate} from "react-router-dom";
import {Button} from "@mui/material";


export default function Choice() {
    const navigate = useNavigate();


    const handleOrg = () => {
        navigate('/setupOrg')
    }

    const handleClient = () => {
        navigate('/setup')
    }

    return (
        <div className="w-screen h-screen flex flex-col items-center justify-center">
            <div
                className="w-1/5 flex flex-col items-center justify-center gap-5"
            >
                <h1 className="mb-5 text-2xl">
                    Register as a
                </h1>
                <Button
                    variant="contained"
                    onClick={handleClient}
                    className="w-full"
                >
                    Client
                </Button>
                <Button
                    variant="contained"
                    onClick={handleOrg}
                    className="w-full"
                >
                    Organization
                </Button>
            </div>
        </div>
    )
}