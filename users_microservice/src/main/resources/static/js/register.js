// ────────────────────────────────────────────────────────────────
// Load privileges when the page loads
// ────────────────────────────────────────────────────────────────
async function loadPrivileges() {
    const select = document.getElementById('privileges');
    if (!select) return;

    select.innerHTML = '<option value="" disabled selected>Loading privileges...</option>';

    try {
        const response = await fetch('http://localhost:8081/dev/privileges/api/printPrivilege', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to load privileges (${response.status})`);
        }

        const data = await response.json();

        // Clear loading state
        select.innerHTML = '<option value="" disabled>Select role / privilege</option>';

        // Normalize to array (in case single object is returned)
        const privilegesList = Array.isArray(data) ? data : [data];

        privilegesList.forEach(item => {
            if (item && typeof item === 'object' && 'id' in item && 'privilegeName' in item) {
                const option = document.createElement('option');
                option.value = item.id;                      // value = id (for easy retrieval)
                option.textContent = item.privilegeName;     // what the user sees
                option.dataset.name = item.privilegeName;    // store name in data attribute
                select.appendChild(option);
            }
        });

        // Optional: auto-select first real option
        if (select.options.length > 1) {
            select.selectedIndex = 1;
        }

        if (privilegesList.length === 0) {
            select.innerHTML = '<option disabled>No privileges available</option>';
        }

    } catch (err) {
        console.error('Error loading privileges:', err);
        select.innerHTML = '<option disabled>Error loading privileges</option>';
    }
}

// ────────────────────────────────────────────────────────────────
// Form submission handler
// ────────────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    loadPrivileges(); // Load privileges immediately

    const form = document.getElementById('registerForm');
    if (!form) return;

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const alertDiv = document.getElementById('registerAlert');
        const submitBtn = document.getElementById('registerBtn');

        // Reset alert
        alertDiv.classList.add('d-none');
        alertDiv.classList.remove('alert-success', 'alert-danger');
        alertDiv.textContent = '';

        // Disable button during request
        submitBtn.disabled = true;
        submitBtn.textContent = 'Registering...';

        // Gather form values
        const fullName        = document.getElementById('fullName')?.value.trim() || '';
        const email           = document.getElementById('email')?.value.trim() || '';
        const cellphone       = document.getElementById('cellphone')?.value.trim() || '';
        const identityNo      = document.getElementById('identityNo')?.value.trim() || '';
        const password        = document.getElementById('password')?.value || '';
        const confirmPassword = document.getElementById('confirmPassword')?.value || '';
        const dob             = document.getElementById('dob')?.value || '';

        // Get selected privilege
        const select = document.getElementById('privileges');
        const selectedOption = select.options[select.selectedIndex];
        const privilegesId   = selectedOption.value ? parseInt(selectedOption.value, 10) : NaN;
        const privilegeName  = selectedOption.dataset.name || selectedOption.textContent || '';

        // Basic client-side validation
        if (password !== confirmPassword) {
            showAlert('Passwords do not match!', 'danger');
            resetButton();
            return;
        }

        if (isNaN(privilegesId) || !privilegeName) {
            showAlert('Please select a privilege / role!', 'danger');
            resetButton();
            return;
        }

        // Prepare payload (now sending object with id + name)
        const payload = {
            userFullName: fullName,
            userEmailAddress: email,
            userCellphoneNo: cellphone,
            userIdentityNo: identityNo,
            userPassword: password,
            confirmPassword: confirmPassword,
            userStatus: 1,               // adjust as needed
            privileges: {
                id: privilegesId,
                privilegeName: privilegeName
            },
            // dateOfBirth: dob,         // uncomment if needed
        };

        try {
            const response = await fetch('http://localhost:8081/dev/api/auth/sign-up', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                showAlert('Registration successful! Redirecting to login...', 'success');
                setTimeout(() => {
                    window.location.href = '/login'; // ← adjust path
                }, 1800);
            } else {
                let errorData = null;
                try {
                    errorData = await response.json();
                } catch {}

                let message = `Registration failed (${response.status})`;

                if (errorData) {
                    if (Array.isArray(errorData) && errorData.length > 0) {
                        message = errorData[0].resolveIssueDetails 
                            || errorData[0].message 
                            || message;
                    } else if (errorData.resolveIssueDetails) {
                        message = errorData.resolveIssueDetails;
                    } else if (errorData.message) {
                        message = errorData.message;
                    } else if (errorData.detail) {
                        message = errorData.detail;
                    }
                }

                showAlert(message, 'danger');
            }
        } catch (err) {
            console.error('Registration error:', err);
            showAlert('Network error – please check your connection and try again.', 'danger');
        } finally {
            resetButton();
        }

        function showAlert(text, type) {
            alertDiv.textContent = text;
            alertDiv.classList.add(`alert-${type}`);
            alertDiv.classList.remove('d-none');
        }

        function resetButton() {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register';
        }
    });
});