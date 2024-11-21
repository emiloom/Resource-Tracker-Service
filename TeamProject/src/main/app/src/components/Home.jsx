import {Button} from "@mui/material";
import {useRef, useState} from "react";
import SearchIcon from '@mui/icons-material/Search';
import {Link, useLocation} from "react-router-dom";
import ReactLoading from 'react-loading';

export default function Home() {

    const location = useLocation();
    const inputRef = useRef(null);

    const [user, setUser] = useState(location.state?.authenticated ?? null);
    const [query, setQuery] = useState("");
    const [loading, setLoading] = useState(false);


    const handleSearchQuery = (e) => {
        e.preventDefault();
        setQuery(e.target.value);
    }

    const handleEnter = (e) => {
        if (e.key === "Enter") {
            handleSearch(e);
            if (inputRef.current) {
                inputRef.current.blur();
            }
        }
    }

    const handleSearch = (e) => {
        e.preventDefault();
        setLoading(true);

        setTimeout(() => {
            setLoading(false);
        }, 1500)
    }

    return (
        <div
            className="w-full h-full"
        >
            <header
                className="border-b-2 border-b-black ~h-12 flex items-center justify-between"
            >
                <div
                    className="flex gap-5 m-5"
                >
                    <Button>
                        Search
                    </Button>
                    {
                        user &&
                        <Button>
                            <Link to="/dashboard">
                                Dashboard
                            </Link>
                        </Button>
                    }
                </div>

                <div
                    className="w-1/2 flex gap-2 items-center "
                >
                    <input
                        ref={inputRef}
                        className="w-11/12 p-2 border-2 border-black"
                        onChange={(e) => handleSearchQuery(e)}
                        type="text"
                        onKeyDown={handleEnter}
                    />
                    <Button
                        onClick={(e) => handleSearch(e)}
                    >
                        <SearchIcon
                            className="w-10 h-10 text-black"
                        />
                    </Button>
                </div>

                <div
                    className="flex gap-5 m-5"
                >
                    {
                        user ?
                            <Button
                                variant="contained"
                            >
                                Logout
                            </Button> :
                            <Button
                                variant="contained"
                            >
                                <Link
                                    to={'/login'}
                                >
                                    Login
                                </Link>
                            </Button>
                    }
                </div>
            </header>

            {/*  Body  */}
            <div
                className="w-full h-full flex justify-center"
            >
                {
                    loading ?
                        <ReactLoading type="spinningBubbles" color="blue" /> :
                        <div>

                        </div>
                }
            </div>
        </div>
    )
}