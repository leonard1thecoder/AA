document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault(); // Prevent page reload

    // Hide previous error
    const errorDiv = document.getElementById('errorAlert');
    errorDiv.classList.add('d-none');
    errorDiv.textContent = '';

    // Get form values
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    // Prepare payload matching your LoginRequest
    const payload = {
        usersEmailAddress: email,
        usersPassword: password
    };

    try {
        const response = await fetch('http://localhost:8081/dev/api/auth/login', {  // ← Replace with real endpoint
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        // Important: fetch only rejects on network failure → we must check status manually
        if (response.ok) {  // 200–299
            /** @type {UsersResponse} */
            const data = await response.json();

            // Success handling
            if (data.token) {
                localStorage.setItem('authToken', data.token); // Or sessionStorage / cookie
                // Optional: store more user info
                // localStorage.setItem('user', JSON.stringify(data));

                alert('Login successful!'); // Replace with real redirect
                window.location.href = '/dashboard'; // ← your success page
            } else {
                throw new Error('No token received from server');
            }
        } else {
            // Non-2xx → try to parse ErrorResponse
            let errorData;
            try {
                errorData = await response.json(); // Should match ErrorResponse
            } catch (jsonErr) {
                // If not valid JSON → fallback
                errorData = { resolveIssueDetails: `Server error (${response.status})` };
            }
			
            // Show the error message from backend
            const message = errorData[0].resolveIssueDetails;

            errorDiv.textContent = message;
            errorDiv.classList.remove('d-none');
        }
    } catch (err) {
        // Network error / CORS / JSON parse fail / etc.
        console.error(err);
        errorDiv.textContent = 'Something went wrong. Please try again later.';
        errorDiv.classList.remove('d-none');
    }
});
