

export async function getType(id) {

    const uid = Number(id) % (2 ** 31);
    try {
        // Check if the UID corresponds to a client
        const clientResponse = await fetch(`https://restinginbed.ue.r.appspot.com/retrieveClient?clientId=${uid}`, {
            method: 'GET',
        });

        if (clientResponse.ok) {
            return 'client';
        }

        // If not a client, check if the UID corresponds to an organization
        const organizationResponse = await fetch(`https://restinginbed.ue.r.appspot.com/retrieveOrganization?organizationId=${uid}`, {
            method: 'GET',
        });

        if (organizationResponse.ok) {
            return 'organization';
        }

        return null;
    } catch (error) {
        console.error(error);
        return null;
    }
}

export async function exists(uid) {
    const userType = await getType(uid);
    return userType !== null;
}

export async function isClient(uid) {
    const userType = await getType(uid);
    return userType === 'client'
}

export async function isOrganization(uid) {
    const userType = await getType(uid);
    return userType === 'organization'
}