import { Button } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ReactLoading from 'react-loading';
import { useRef, useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import SearchResultsTable from './SearchResultsTable';
import Logout from './Logout';

export default function Home() {
    const navigate = useNavigate();
    const inputRef = useRef(null);

    const [query, setQuery] = useState('');
    const [loading, setLoading] = useState(false);
    const [items, setItems] = useState([]);
    const [organizations, setOrganizations] = useState({});
    const [cookies, setCookies] = useCookies(['auth_token', 'exp_time', 'uid']);

    const user = cookies.auth_token;

    useEffect(() => {
        const fetchOrganizations = async () => {
            try {
                const response = await fetch('https://restinginbed.ue.r.appspot.com/organizations');
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
            // Fetch search results
            const response = await fetch(`https://restinginbed.ue.r.appspot.com/searchItems?searchTerm=${query}`);
            if (!response.ok) throw new Error('Search failed');
            const data = await response.json();

            // Fetch distances for ranking
            const originId = Number(cookies.uid) % (2 ** 31);;
            const originType = 'client';
            const rankResponse = await fetch(
                `https://restinginbed.ue.r.appspot.com/rankNearestOrganizations?originId=${originId}&originType=${originType}`
            );
            if (!rankResponse.ok) throw new Error('Failed to fetch ranked distances');
            const rankedDistances = await rankResponse.json();

            // Create a map of organization ID to distance
            const distanceMap = rankedDistances.reduce((acc, dto) => {
                acc[dto.organization.id] = dto.distance;
                return acc;
            }, {});

            // Augment items with distances
            const rankedItems = data.map(item => ({
                ...item,
                distance: distanceMap[item.organizationId] || Infinity, // Default to Infinity if no distance is found
            }));

            // Sort items by distance
            rankedItems.sort((a, b) => a.distance - b.distance);

            setItems(rankedItems);
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
