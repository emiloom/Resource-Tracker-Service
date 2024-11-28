import {Button} from "@mui/material";
import {Link} from "react-router-dom";
import SearchIcon from "@mui/icons-material/Search";

export default function Header() {


    return (
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
    )
}