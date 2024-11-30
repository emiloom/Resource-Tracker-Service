import {
    Button,
    Dialog,
    DialogContent,
    IconButton,
    TextField
} from "@mui/material";
import { Dropdown } from '@mui/base/Dropdown';
import { MenuButton } from '@mui/base/MenuButton';
import { Menu } from '@mui/base/Menu';
import { MenuItem } from '@mui/base/MenuItem';
import {useEffect, useRef, useState} from "react";
import {useCookies} from "react-cookie";
import {Link, useNavigate} from "react-router-dom";
import Logout from "./Logout.jsx";
import {Close, Delete, Mode} from "@mui/icons-material";
import {FormControl, TextareaAutosize} from "@mui/base";
import {NumberInput} from "@mui/base/Unstable_NumberInput/NumberInput.js";
import SearchIcon from "@mui/icons-material/Search";
import ReactLoading from 'react-loading';



export default function Dashboard () {

    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid']);
    const [selectedFilter, setSelectedFilter] = useState("Filters");
    const [userData, setUserData] = useState();
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [error, setError] = useState({ itemName: false, count: false, desc: false, location: false });
    const [editErrors, setEditErrors] = useState({itemName: false, count: false, desc: false, location: false })
    const [itemName, setItemName] = useState("");
    const [count, setCount] = useState(0);
    const [desc, setDesc] = useState("");
    const [location, setLocation] = useState("");
    const [refresh, setRefresh] = useState(false);
    const [query, setQuery] = useState("");
    const [loading, setLoading] = useState(false);
    const [filteredData, setFilteredData] = useState();
    const [editObj, setEditObj] = useState(null);

    const inputRef = useRef();

    const navigate = useNavigate();

    const user = cookies.uid;

    useEffect(() => {
        if (!user) {
            navigate('/login');
        }
    }, [user]);

    const createHandleMenuClick = (menuItem) => {
        return () => {
            setSelectedFilter(menuItem);

            setLoading(true);


            setTimeout(() => {
                let sortedData;
                if (menuItem === "High to Low") {
                    sortedData = [...userData].sort((a, b) => b.count - a.count);
                } else if (menuItem === "Low to High") {
                    sortedData = [...userData].sort((a, b) => a.count - b.count);
                }

                setFilteredData(sortedData);
                setLoading(false);
            }, 1000);
        };
    };



    const handleRemoveFilter = () => {
        setSelectedFilter("Filters");
        handleSearch();
    }

    const handleModal = () => {
        setIsAddModalOpen(true);
    }

    const handleCloseAddModal = () => {
        setIsAddModalOpen(false);
        setError({ itemName: false, count: false, desc: false, location: false });
        setItemName("");
        setCount(0);
        setDesc("");
        setLocation("");
    }

    const handleAddItem = () => {
        let hasError = false;

        if (!itemName) {
            setError((prev) => ({ ...prev, itemName: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, itemName: false }));
        }

        if (!count) {
            setError((prev) => ({ ...prev, count: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, count: false }));
        }

        if (!desc) {
            setError((prev) => ({ ...prev, desc: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, desc: false }));
        }

        if (!location) {
            setError((prev) => ({ ...prev, location: true }));
            hasError = true;
        } else {
            setError((prev) => ({ ...prev, location: false }));
        }

        if (hasError) return;

        const organizationId = Number(cookies.uid) % (2 ** 31);
        const newItem = {
            name: itemName,
            description: desc,
            count: count,
            organizationId: organizationId,
            location: location
        }

        setLoading(true);
        fetch('https://restinginbed.ue.r.appspot.com/createItem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newItem),
        })
            .then(response => {
                if (response.ok) {
                    setRefresh(prev => !prev);
                } else {
                }

                setLoading(false);
            });

        handleCloseAddModal();

    }

    const handleSearchQuery = (e) => {
        e.preventDefault();
        setQuery(e.target.value);
    }

    const handleEnter = (e) => {
        if (e.key === "Enter") {
            handleSearch();
            if (inputRef.current) {
                inputRef.current.blur();
            }
        }
    }

    const handleSearch = () => {
        setLoading(true);

        if (!query) {
            setFilteredData(userData);
            setLoading(false);
            return
        }

        const searchQuery = query.toLowerCase();

        setTimeout(() => {
            setLoading(false);

            const newfilteredData = filteredData.filter(
                (obj) =>
                    obj.name.toLowerCase().includes(searchQuery) ||
                    obj.location.toLowerCase().includes(searchQuery) ||
                    obj.description.toLowerCase().includes(searchQuery)
            );

            setFilteredData(newfilteredData);
        }, 500);
    };

    const handleDeleteItem = async (objId) => {
        try {
            const itemToDelete = userData.find(obj => obj.id === objId);

            if (!itemToDelete) {
                console.error(`Item with id ${objId} not found`);
                return;
            }

            const response = await fetch(`https://restinginbed.ue.r.appspot.com/deleteItem/${objId}`, {
                method: 'DELETE',
            });

            if (!response.ok) {
                if (response.status === 404) {
                    console.error("Item not found on the server");
                } else {
                    console.error("Error deleting item");
                }
                return;
            }

            const updatedUserData = userData.filter(obj => obj.id !== objId);
            setUserData(updatedUserData);
            setFilteredData(updatedUserData);
        } catch (error) {
            console.error("Error while deleting item:", error);
        }
    };


    const handleEditItem = (objId) => {
        const editObj = userData.find((obj) => obj.id === objId);

        if (!editObj) {
            console.error(`Object with id ${objId} not found`);
            return;
        }

        setItemName(editObj.name);
        setCount(editObj.count);
        setDesc(editObj.description);
        setLocation(editObj.location);
        setEditObj(objId);

        setIsEditModalOpen(true);
    };


    const handleEditModalClose = () => {
        setIsEditModalOpen(false);
        setEditObj(null);
        setEditErrors({ itemName: false, count: false, desc: false, location: false });
        setItemName("");
        setCount(0);
        setDesc("");
        setLocation("");
    }

    const handleUpdateItem = () => {
        let hasError = false;

        if (!itemName) {
            setEditErrors((prev) => ({ ...prev, itemName: true }));
            hasError = true;
        } else {
            setEditErrors((prev) => ({ ...prev, itemName: false }));
        }

        if (!count) {
            setEditErrors((prev) => ({ ...prev, count: true }));
            hasError = true;
        } else {
            setEditErrors((prev) => ({ ...prev, count: false }));
        }

        if (!desc) {
            setEditErrors((prev) => ({ ...prev, desc: true }));
            hasError = true;
        } else {
            setEditErrors((prev) => ({ ...prev, desc: false }));
        }

        if (!location) {
            setEditErrors((prev) => ({ ...prev, location: true }));
            hasError = true;
        } else {
            setEditErrors((prev) => ({ ...prev, location: false }));
        }

        if (hasError) return;


        const organizationId = Number(cookies.uid) % (2 ** 31);
        const updatedItem = {
            name: itemName,
            description: desc,
            count: count,
            organizationId: organizationId,
            location: location
        };

        setLoading(true);
        fetch(`https://restinginbed.ue.r.appspot.com/updateItem/${editObj}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedItem),
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else if (response.status === 404) {
                    console.error('Item not found');
                    throw new Error('Item not found');
                } else {
                    console.error('Failed to update item');
                    throw new Error('Failed to update item');
                }
            })
            .then((data) => {
                setRefresh((prev) => !prev);
            })
            .catch((error) => {
                console.error('Error updating item:', error);
            })
            .finally(() => {
                setLoading(false);
            });

        handleEditModalClose();

    }


    useEffect(() => {
        if (!user) return;

        const organizationId = Number(user) % (2 ** 31);

        fetch(`https://restinginbed.ue.r.appspot.com/organizations/${organizationId}/items`)
            .then((response) => {
                if (response.status === 404) {
                    return null;
                }
                return response.json();
            })
            .then((data) => {
                    setUserData(data);
                    setFilteredData(data);
            })
            .catch((error) => {
                console.error("Error during fetch:", error);
            });
    }, [user, refresh]);

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
                    className="grid grid-cols-12 gap-4 w-full h-10 m-4"
                >
                    <div className="col-span-2">
                        <Dropdown>
                            <MenuButton
                                className="hover: cursor-pointer p-2"
                            >
                                {selectedFilter}
                            </MenuButton>
                            <Menu>
                                <MenuItem
                                    className="hover:cursor-pointer p-2 bg-white hover:bg-gray-100"
                                    onClick={createHandleMenuClick('High to Low')}>High to Low</MenuItem>
                                <MenuItem
                                    className="hover:cursor-pointer p-2 bg-white hover:bg-gray-100"
                                    onClick={createHandleMenuClick('Low to High')}>Low to High</MenuItem>
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

                    <div
                        className="flex col-span-9 gap-2 items-center "
                    >
                        <input
                            ref={inputRef}
                            className="w-11/12 p-2 border-2 border-black"
                            onChange={(e) => handleSearchQuery(e)}
                            type="text"
                            onKeyDown={(e) => handleEnter(e)}
                        />
                        <Button
                            onClick={handleSearch}
                        >
                            <SearchIcon
                                className="w-10 h-10 text-black"
                            />
                        </Button>
                    </div>

                    <Button
                        variant="outlined"
                        onClick={handleModal}

                    >
                        Add
                    </Button>
                </div>

            </div>

            {
                loading ?
                    <div
                        className="w-full h-[80%] flex items-center justify-center"
                    >
                        <ReactLoading type="spinningBubbles" color="blue" />
                    </div> :
                    <div
                        className={`p-4 w-full h-[80%] ${userData ? "" : "flex items-center justify-center"}`}>
                        {
                            filteredData && filteredData.length > 0 ? (
                                <table
                                    className="table-auto w-full border-collapse border border-gray-300 shadow-md rounded-md">
                                    <thead className="bg-gray-100">
                                    <tr>
                                        <th className="px-4 py-2 text-left border border-gray-300 font-semibold">#</th>
                                        <th className="px-4 py-2 text-left border border-gray-300 font-semibold">Name</th>
                                        <th className="px-4 py-2 text-left border border-gray-300 font-semibold">Description</th>
                                        <th className="px-4 py-2 text-left border border-gray-300 font-semibold">Location</th>
                                        <th className="px-4 py-2 text-left border border-gray-300 font-semibold">Count</th>
                                        <th className="px-2 py-2 text-center border border-gray-300 font-semibold w-[120px]">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {filteredData.map((obj, index) => (
                                        <tr
                                            key={index}
                                            className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-gray-50' : 'bg-white'}`}>
                                            <td className="px-4 py-2 border border-gray-300">{index + 1}</td>
                                            <td className="px-4 py-2 border border-gray-300">{obj.name}</td>
                                            <td className="px-4 py-2 border border-gray-300">{obj.description}</td>
                                            <td className="px-4 py-2 border border-gray-300">{obj.location}</td>
                                            <td className="px-4 py-2 border border-gray-300">{obj.count}</td>
                                            <td className="px-2 py-2 border border-gray-300 w-[120px]">
                                                <div
                                                    className="flex justify-evenly items-center space-x-2">
                                                    <Mode className="text-blue-400 hover:cursor-pointer hover:text-blue-600"
                                                          onClick={() => handleEditItem(obj.id)}
                                                    />
                                                    <Delete className="text-red-400 hover:cursor-pointer hover:text-red-600"
                                                        onClick={() => handleDeleteItem(obj.id)}
                                                    />
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>

                            ) : (
                                <p className="text-center text-gray-500">No items found...</p>
                            )
                        }
                    </div>
            }


            {/* Add Item Modal */}
            {
                <Dialog
                    open={isAddModalOpen}
                    onClose={handleCloseAddModal}
                >
                    <div className="flex items-baseline gap-4">
                        <p className="p-5">Add an Item</p>
                        <IconButton
                            onClick={handleCloseAddModal}
                            sx={(theme) => ({
                                position: 'absolute',
                                right: 8,
                                top: 8,
                                color: theme.palette.grey[500],
                            })}
                        >
                            <Close/>
                        </IconButton>
                    </div>
                    <DialogContent className="w-[500px] p-6">
                        <form action="" className="flex flex-col gap-6">
                            <FormControl className="flex flex-col gap-4">
                                {/* Name Input */}
                                <TextField
                                    label="Name"
                                    variant="outlined"
                                    className="w-full text-base font-sans border rounded-md shadow-sm focus:ring focus:ring-blue-500"
                                    error={error.itemName}
                                    onChange={(e) => setItemName(e.target.value)}
                                    required
                                />
                                {error.itemName && <span className="text-red-500 text-sm">Name is required</span>}

                                {/* Location Input */}
                                <TextField
                                    label="Location"
                                    variant="outlined"
                                    className="w-full text-base font-sans border rounded-md shadow-sm focus:ring focus:ring-blue-500"
                                    error={error.itemName}
                                    onChange={(e) => setLocation(e.target.value)}
                                    required
                                />
                                {error.location && <span className="text-red-500 text-sm">Name is required</span>}

                                {/* Number Input */}
                                <div className="flex flex-col gap-1">
                                    <label className="font-medium text-sm">
                                        Count <span>*</span>
                                    </label>
                                    <NumberInput
                                        placeholder="Count"
                                        slotProps={{
                                            input: {
                                                className: `w-full px-3 py-2 border rounded-md ${
                                                    error.count ? 'border-red-500' : 'border-gray-300'
                                                }`,
                                            },
                                        }}
                                        className={`w-full rounded-md focus:ring-2 ${
                                            error.count ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                        }`}
                                        onChange={(e) => setCount(e.target.value)}
                                    />
                                    {error.count && <span className="text-red-500 text-sm">Count is required</span>}
                                </div>

                                {/* Description Textarea */}
                                <div className="flex flex-col gap-1">
                                    <label className="font-medium text-sm">
                                        Description <span>*</span>
                                    </label>
                                    <TextareaAutosize
                                        placeholder="Description"
                                        className={`w-full text-base font-sans px-3 py-2 border rounded-md shadow-sm focus:ring ${
                                            error.desc ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'
                                        }`}
                                        minRows={4}
                                        onChange={(e) => setDesc(e.target.value)}
                                    />
                                    {error.desc && <span className="text-red-500 text-sm">Description is required</span>}
                                </div>


                                <Button
                                    variant="contained"
                                    onClick={handleAddItem}
                                >
                                    Add
                                </Button>
                            </FormControl>
                        </form>
                    </DialogContent>
                </Dialog>
            }

            <Dialog
                open={isEditModalOpen}
                onClose={handleEditModalClose}
            >
                <div className="flex items-baseline gap-4">
                    <p className="p-5">Edit Item</p>
                    <IconButton
                        onClick={handleEditModalClose}
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
                <DialogContent className="w-[500px] p-6">
                    <form action="" className="flex flex-col gap-6">
                        <FormControl className="flex flex-col gap-4">
                            {/* Name Input */}
                            <TextField
                                label="Name"
                                variant="outlined"
                                className="w-full text-base font-sans border rounded-md shadow-sm focus:ring focus:ring-blue-500"
                                value={itemName}
                                error={editErrors.itemName}
                                onChange={(e) => setItemName(e.target.value)}
                                required
                            />
                            {editErrors.itemName && <span className="text-red-500 text-sm">Name is required</span>}

                            {/* Location Input */}
                            <TextField
                                label="Location"
                                variant="outlined"
                                className="w-full text-base font-sans border rounded-md shadow-sm focus:ring focus:ring-blue-500"
                                value={location}
                                error={editErrors.location}
                                onChange={(e) => setLocation(e.target.value)}
                                required
                            />
                            {editErrors.location && <span className="text-red-500 text-sm">Location is required</span>}

                            {/* Number Input */}
                            <div className="flex flex-col gap-1">
                                <label className="font-medium text-sm">
                                    Count <span>*</span>
                                </label>
                                <NumberInput
                                    placeholder="Count"
                                    slotProps={{
                                        input: {
                                            className: `w-full px-3 py-2 border rounded-md ${
                                                editErrors.count ? 'border-red-500' : 'border-gray-300'
                                            }`,
                                        },
                                    }}
                                    className={`w-full rounded-md focus:ring-2 ${
                                        editErrors.count ? 'focus:ring-red-500 focus:border-red-500' : 'focus:ring-blue-500 focus:border-blue-500'
                                    }`}
                                    onChange={(e) => setCount(e.target.value)}
                                />
                                {editErrors.count && <span className="text-red-500 text-sm">Count is required</span>}
                            </div>

                            {/* Description Textarea */}
                            <div className="flex flex-col gap-1">
                                <label className="font-medium text-sm">
                                    Description <span>*</span>
                                </label>
                                <TextareaAutosize
                                    placeholder="Description"
                                    value={desc}
                                    className={`w-full text-base font-sans px-3 py-2 border rounded-md shadow-sm focus:ring ${
                                        editErrors.desc ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-blue-500'
                                    }`}
                                    minRows={4}
                                    onChange={(e) => setDesc(e.target.value)}
                                />
                                {editErrors.desc && <span className="text-red-500 text-sm">Description is required</span>}
                            </div>

                            {/* Save Changes Button */}
                            <Button
                                variant="contained"
                                onClick={handleUpdateItem}
                            >
                                Save Changes
                            </Button>
                        </FormControl>
                    </form>
                </DialogContent>
            </Dialog>


        </div>
    )
}