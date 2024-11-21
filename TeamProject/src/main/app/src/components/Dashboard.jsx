import {Button} from "@mui/material";
import { Dropdown } from '@mui/base/Dropdown';
import { MenuButton } from '@mui/base/MenuButton';
import { Menu } from '@mui/base/Menu';
import { MenuItem } from '@mui/base/MenuItem';
import { useState } from "react";


export default function Dashboard () {

    const [selectedFilter, setSelectedFilter] = useState("Filters")

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
        <div
            className="w-screen h-screen flex"
        >
            <header
                className="w-full h-10 flex justify-between m-4"
            >
                <div>
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
            </header>

            <div>

            </div>
        </div>
    )
}