import { Button } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ReactLoading from 'react-loading';
import { useRef, useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import SearchResultsTable from './SearchResultsTable';

export default function Home() {
    const navigate = useNavigate();
    const inputRef = useRef(null);

    const [query, setQuery] = useState('');
    const [loading, setLoading] = useState(false);
    const [items, setItems] = useState([]);
    const [organizations, setOrganizations] = useState({});
    const [cookies] = useCookies(['auth_token']);

    const user = cookies.auth_token;

    useEffect(() => {
        const fetchOrganizations = async () => {
            try {
                const response = await fetch('http://localhost:8080/organizations');
                if (!response.ok) throw new Error('Failed to fetch organizations');
                const orgs = await response.json();
                const orgMap = orgs.reduce((acc, org) => {
                    acc[org.id] = { name: org.name, location: org.location }; // Store name and location
                    return acc;
                }, {});
                setOrganizations(orgMap);
            } catch (error) {
                console.error('Error fetching organizations:', error);
            }
        };

        fetchOrganizations();
    }, []);

    const handleSearchQuery = (e) => {
        setQuery(e.target.value);
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/searchItems?searchTerm=${query}`);
            if (!response.ok) throw new Error('Search failed');
            const data = await response.json();
            setItems(data);
        } catch (error) {
            console.error('Error:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleEnter = (e) => {
        if (e.key === 'Enter') handleSearch(e);
    };

    return (
        <div className="w-full h-full">
            <header className="border-b-2 border-b-black ~h-12 flex items-center justify-between">
                <div className="flex gap-5 m-5">
                    <Button>Search</Button>
                    {user && (
                        <Link to="/dashboard">
                            <Button>Dashboard</Button>
                        </Link>
                    )}
                </div>

                <div className="w-1/2 flex gap-2 items-center">
                    <input
                        ref={inputRef}
                        className="w-11/12 p-2 border-2 border-black"
                        type="text"
                        value={query}
                        onChange={handleSearchQuery}
                        onKeyDown={handleEnter}
                    />
                    <Button onClick={handleSearch}>
                        <SearchIcon className="w-10 h-10 text-black" />
                    </Button>
                </div>

                <div className="flex gap-5 m-5">
                    {user ? (
                        <Button variant="contained">Logout</Button>
                    ) : (
                        <Link to="/login">
                            <Button variant="contained">Login</Button>
                        </Link>
                    )}
                </div>
            </header>

            <main className="w-full h-full flex justify-center p-4">
                {loading ? (
                    <ReactLoading type="spinningBubbles" color="blue" />
                ) : (
                    <SearchResultsTable items={items} organizations={organizations} />
                )}
            </main>
        </div>
    );
}
