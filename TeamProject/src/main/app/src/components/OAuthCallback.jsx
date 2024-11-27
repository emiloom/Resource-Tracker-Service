import React, { useEffect } from "react";

function OAuthCallback({ onError }) {
    useEffect(() => {

        const hashParams = new URLSearchParams(window.location.hash.substring(1));
        const accessToken = hashParams.get("access_token");
        const idToken = hashParams.get("id_token");
        const expiresIn = hashParams.get("expires_in");

        if (accessToken) {
            console.log("Tokens extracted in popup:", { accessToken, idToken, expiresIn });

            if (window.opener) {
                window.opener.postMessage(
                    { accessToken, idToken, expiresIn },
                    window.opener.origin
                );

                window.close();
            } else {
                onError();
                console.error("No parent window to send tokens to.");
            }
        }
    }, []);

    return (
        <div>
            <h1>Processing...</h1>
            <p>If you are not redirected automatically, please close this window.</p>
        </div>
    );
};

export default OAuthCallback;
