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
    const [organizations, setOrganizations] = useState({});
    const [query, setQuery] = useState('');
    const [loading, setLoading] = useState(false);
    const [items, setItems] = useState([]);
    const [cookies] = useCookies(['auth_token', 'exp_time', 'uid', 'type']);
    const user = cookies.uid;
    const type = cookies.type;
    const [shownItems, setShownItems] = useState([])

    const handleSearchQuery = (e) => {
        setQuery(e.target.value);
    };


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

    const fetchDistance = async (originId, originType, destId, destType) => {
        try {
            console.log(typeof originId, typeof originType, typeof destId, typeof destType);

            const response = await fetch(
                `https://restinginbed.ue.r.appspot.com/resolveDistance?originId=${originId}&originType=${originType}&destId=${destId}&destType=${destType}`
            );

            if (!response.ok) {
                throw new Error('Failed to fetch distance');
            }

            const data = await response.json();
            console.log(originId, destId, data);

            return data; // Ensure you are returning the `distance` field here
        } catch (error) {
            console.error('Error fetching distance:', error);
            return null; // Return null or a default value in case of an error
        }
    };


    const handleSearch = async () => {
        try {
            setLoading(true);
            const itemResponse = await fetch(
                `https://restinginbed.ue.r.appspot.com/searchItems?searchTerm=${query}`
            );
            if (!itemResponse.ok) {
                const errorBody = await itemResponse.text();
                throw new Error(`Failed to fetch items: ${errorBody}`);
            }
            const items = await itemResponse.json();

            console.log(items)

            const clientId = Number(user) % (2 ** 31);

            const itemsWithDistances = await Promise.all(
                items.map(async (item) => {
                    const distance = await fetchDistance(
                        clientId,
                        type,
                        item.organizationId,
                        'organization'
                    );
                    console.log('before distances', distance);
                    return { ...item, distance };
                })
            );

            // Sort the items by their 'distance' field
            const sortedItems = itemsWithDistances.sort((a, b) => a.distance - b.distance);

            // Set the sorted items to the state
            setShownItems(sortedItems);
            console.log(sortedItems, 'sorted');
        } catch (error) {
            console.error(error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleEnter = (e) => {
        if (e.key === 'Enter') handleSearch(e);
    };

    useEffect(() => {
        if (query !== "") return
        fetch(`https://restinginbed.ue.r.appspot.com/organizations/-1/items`, {
            method: 'GET'
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
        })
            .then(async data => {
                console.log(data, 'unsorted');
                const clientId = Number(user) % (2 ** 31);

                // Map through the items and add a 'distance' field using fetchDistance
                const itemsWithDistances = await Promise.all(
                    data.map(async (item) => {
                        const distance = await fetchDistance(
                            clientId,
                            type,
                            item.organizationId,
                            'organization'
                        );
                        console.log('before distances', distance);
                        return { ...item, distance };
                    })
                );

                // Sort the items by their 'distance' field
                const sortedItems = itemsWithDistances.sort((a, b) => a.distance - b.distance);

                // Set the sorted items to the state
                setShownItems(sortedItems);
                console.log(sortedItems, 'sorted');

            })
    }, [query]);

    return (
        <div className="w-full h-full">
            <header className="border-b-2 border-b-black ~h-12 flex items-center justify-between">
                <div
                    className="flex gap-5 m-5"
                >
                    <Link to="/">
                        <Button>
                            Search
                        </Button>
                    </Link>
                    {
                        type === 'organization' &&
                        <Link to="/dashboard">
                            <Button>
                                Dashboard
                            </Button>
                        </Link>
                    }
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
                        <Logout />
                    ) : (
                        <Button variant="contained">
                            <Link to={'/login'}>Login</Link>
                        </Button>
                    )}
                </div>
            </header>

            <main className="w-full h-full flex justify-center p-4">
                {loading ? (
                    <ReactLoading type="spinningBubbles" color="blue" />
                ) : (
                    <SearchResultsTable items={shownItems} organizations={organizations} />
                )}
            </main>
        </div>
    );
}
