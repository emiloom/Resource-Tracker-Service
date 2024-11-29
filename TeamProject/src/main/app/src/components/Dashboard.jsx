import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
    TextField
} from "@mui/material";
import { Dropdown } from '@mui/base/Dropdown';
import { MenuButton } from '@mui/base/MenuButton';
import { Menu } from '@mui/base/Menu';
import { MenuItem } from '@mui/base/MenuItem';
import {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {Link, useNavigate} from "react-router-dom";
import Logout from "./Logout.jsx";
import {Close} from "@mui/icons-material";
import {FormControl, TextareaAutosize} from "@mui/base";
import {NumberInput} from "@mui/base/Unstable_NumberInput/NumberInput.js";


export default function Dashboard () {

    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid']);
    const [selectedFilter, setSelectedFilter] = useState("Filters");
    const [userData, setUserData] = useState();
    const [isOpen, setIsOpen] = useState(false);

    const navigate = useNavigate();

    const user = cookies.uid;

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

    const handleModal = () => {
        setIsOpen(true);
    }

    const handleClose = () => {
        setIsOpen(false);
    }

    useEffect(() => {
        if (!user) return;

        // Cast the Long to an Integer using modulo
        const organizationId = Number(user) % (2 ** 31);

        fetch(`http://localhost:8080/organizations/${organizationId}/items`)
            .then((response) => {
                console.log(response.status)
                if (response.status === 404) {
                    console.log('not found')
                    return null;
                }
                return response.json();
            })
            .then((data) => {
                    setUserData(data);
            })
            .catch((error) => {
                console.error("Error during fetch:", error);
            });
    }, [user]);

    useEffect(()=> {
        console.log('data cahnge', userData);
    }, [userData])

    return (
        <div className="h-full w-full">
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
                className="flex h-1/12"
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
                    <Button
                        variant="outlined"
                        onClick={handleModal}
                    >
                        Add
                    </Button>
                </div>

            </div>

            <div className={`p-4 w-full h-[80%] ${userData ? "" : "flex items-center justify-center"}`}>
                {
                    userData ?
                        userData :
                        <p>No items found...</p>
                }
            </div>

            {
                <Dialog
                    open={isOpen}
                >
                    <div className="flex items-baseline gap-4">
                        <p className="p-5">Add an Item</p>
                        <IconButton
                            onClick={handleClose}
                            sx={(theme) => ({
                                position: 'absolute',
                                right: 8,
                                top: 8,
                                color: theme.palette.grey[500],
                            })}
                        >
                            <Close />
                        </IconButton>
                    </div>
                    <DialogContent className="w-[500px]">
                        <form action="">
                            <FormControl className="flex flex-col gap-4">
                                <TextField label="Name" />
                                <NumberInput placeholder="Count"
                                    className="col-start-1 col-end-2 row-start-1 row-end-3 text-sm font-sans leading-normal text-slate-900 bg-inherit border-0 rounded-[inherit] dark:text-slate-300 px-3 py-2 outline-0 focus-visible:outline-0 focus-visible:outline-none"
                                />
                                <TextareaAutosize label="Description" />

                            </FormControl>
                        </form>
                    </DialogContent>
                </Dialog>
            }
        </div>
    )
}