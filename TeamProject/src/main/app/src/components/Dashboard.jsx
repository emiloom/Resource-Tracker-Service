import {Button} from "@mui/material";
import { Dropdown } from '@mui/base/Dropdown';
import { MenuButton } from '@mui/base/MenuButton';
import { Menu } from '@mui/base/Menu';
import { MenuItem } from '@mui/base/MenuItem';
import {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {Link, useNavigate} from "react-router-dom";
import Logout from "./Logout.jsx";


export default function Dashboard () {

    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time']);
    const [selectedFilter, setSelectedFilter] = useState("Filters");

    const navigate = useNavigate();

    const user = cookies.auth_token;

    useEffect(() => {
        if (!user) {
            navigate('/login');
        }
    }, [user]);

    const createHandleMenuClick = (menuItem) => {
        return () => {
            console.log(`clicked on ${menuItem}`)
            setSelectedFilter(menuItem)
        }
    }

    const handleRemoveFilter = () => {
        setSelectedFilter("Filters")
    }

    return (
        <>
            <header
                className="border-b-2 border-b-black ~h-12 flex items-center justify-between"
            >
                <div
                    className="flex gap-5 m-5"
                >
                    <Link to="/">
                        <Button>
                            Search
                        </Button>
                    </Link>
                    {
                        user &&
                        <Link to="/dashboard">
                            <Button>
                                Dashboard
                            </Button>
                        </Link>
                    }
                </div>


                <div
                    className="flex gap-5 m-5"
                >
                    {
                        user ?
                            <Logout /> :
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

            <div
                className="flex"
            >
                <div
                    className="w-full h-10 flex justify-between m-4"
                >
                    <Dropdown>
                        <MenuButton
                            className="hover: cursor-pointer p-2"
                        >
                            {selectedFilter}
                        </MenuButton>
                        <Menu>
                            <MenuItem
                                className="hover: cursor-pointer p-2 bg-blue-400 text-white hover:bg-blue-500"
                                onClick={createHandleMenuClick('Filter 1')}>Filter 1</MenuItem>
                            <MenuItem
                                className="hover: cursor-pointer p-2 bg-blue-400 text-white hover:bg-blue-500"
                                onClick={createHandleMenuClick('Filter 2')}>Filter 2</MenuItem>
                            <MenuItem
                                className="hover: cursor-pointer p-2 bg-blue-400 text-white hover:bg-blue-500"
                                onClick={createHandleMenuClick('Filter 3')}>Filter 3</MenuItem>
                        </Menu>
                    </Dropdown>
                    {
                        selectedFilter !== "Filters" &&
                        <Button
                            variant="text"
                            onClick={handleRemoveFilter}
                        >
                            Remove Filter
                        </Button>
                    }
                </div>

                <Button
                    variant="outlined"
                >
                    Add
                </Button>
            </div>

            <div>

            </div>
        </>
    )
}