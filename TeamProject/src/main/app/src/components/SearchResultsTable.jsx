import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography,
    Button,
} from '@mui/material';

export default function SearchResultsTable({ items, organizations }) {

    const handleOpenMaps = (item) => {
        window.open(`http://maps.google.com/?q=${item.location}`, "_blank")
    }


    return (
        <TableContainer component={Paper} sx={{ mt: 3, boxShadow: 3 }}>
            <Typography variant="h6" sx={{ p: 2, backgroundColor: '#1976d2', color: 'white', textAlign: 'center' }}>
                Search Results
            </Typography>

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Name</TableCell>
                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Description</TableCell>
                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Count</TableCell>
                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Organization</TableCell>
                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Location</TableCell>
                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Open in GMaps</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {items.length > 0 ? (
                        items.map((item) => {
                            const organization = organizations[item.organizationId] || {};
                            return (
                                <TableRow key={item.id}>
                                    <TableCell align="center">{item.name}</TableCell>
                                    <TableCell align="center">{item.description}</TableCell>
                                    <TableCell align="center">{item.count}</TableCell>
                                    <TableCell align="center">{organization.name || 'Unknown Organization'}</TableCell>
                                    <TableCell align="center">{organization.location || 'Unknown Location'}</TableCell>
                                    <TableCell align="center">
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={() => handleOpenMaps(item)}
                                        >
                                            Open
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            );
                        })
                    ) : (
                        <TableRow>
                            <TableCell colSpan={5} align="center">
                                No results found.
                            </TableCell>
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    );
}
