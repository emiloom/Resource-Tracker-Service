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
    const [cookies] = useCookies(['auth_token', 'exp_time', 'uid']);
    const user = cookies.uid;

    const handleSearchQuery = (e) => {
        setQuery(e.target.value);
    };

    const fetchDistance = async (originId, originType, destId, destType) => {
        const response = await fetch(
            `https://restinginbed.ue.r.appspot.com/resolveDistance?originId=${originId}&originType=${originType}&destId=${destId}&destType=${destType}`
        );
        if (!response.ok) throw new Error('Failed to fetch distance');
        return await response.json();
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

            const distances = await Promise.all(
                items.map(async (item) => {
                    const distance = await fetchDistance(
                        '2106589184', // Assuming the user's ID is the origin
                        'organization',   // Assuming user type is 'client'
                        item.organizationId,
                        'organization'
                    );
                    return { ...item, distance };
                })
            );

            const sortedItems = distances.sort((a, b) => a.distance - b.distance);
            setItems(sortedItems);
        } catch (error) {
            console.error(error.message);
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
                    <Button onClick={handleSearch}>Search</Button>
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
                    <SearchResultsTable items={items} />
                )}
            </main>
        </div>
    );
}
