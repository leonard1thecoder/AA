// ────────────────────────────────────────────────────────────────
// Reusable showAlert with scroll
// ────────────────────────────────────────────────────────────────
function showAlert(text, type = 'danger') {
    const alertDiv = document.getElementById('registerAlert');
    if (!alertDiv) {
        console.error('registerAlert element not found');
        return;
    }

    // Reset classes & set new style
    alertDiv.textContent = text;
    alertDiv.className = `alert alert-${type} mb-3`;  // clean reset
    alertDiv.classList.remove('d-none');

    // Force browser reflow (critical after display change)
    void alertDiv.offsetHeight;

    // Scroll after short delay (timing fix for most cases)
    setTimeout(() => {
        alertDiv.scrollIntoView({
            behavior: 'smooth',
            block: 'center',     // 'center' / 'nearest' / 'start' — try different if needed
            inline: 'nearest'
        });

        // Optional fallback if scrollIntoView fails (e.g. fixed header issue)
        // Uncomment and adjust offset if alert hides behind navbar
        /*
        const rect = alertDiv.getBoundingClientRect();
        const offset = -100; // negative = scroll higher (navbar height + margin)
        window.scrollTo({
            top: rect.top + window.scrollY + offset,
            behavior: 'smooth'
        });
        */
    }, 150);   // 120–200 ms — increase to 250/300 if still not working
}

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
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(`Failed to load privileges (${response.status})`);
        }

        const data = await response.json();

        select.innerHTML = '<option value="" disabled>Select role / privilege</option>';

        const privilegesList = Array.isArray(data) ? data : [data];

        privilegesList.forEach(item => {
            if (item && typeof item === 'object' && 'id' in item && 'privilegeName' in item) {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.privilegeName;
                option.dataset.name = item.privilegeName;
                select.appendChild(option);
            }
        });

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
    loadPrivileges();

    const form = document.getElementById('registerForm');
    if (!form) return;

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const alertDiv  = document.getElementById('registerAlert');
        const submitBtn = document.getElementById('registerBtn');

        // Reset alert
        alertDiv.classList.add('d-none');
        alertDiv.classList.remove('alert-success', 'alert-danger');
        alertDiv.textContent = '';

        submitBtn.disabled = true;
        submitBtn.textContent = 'Registering...';

        const fullName        = document.getElementById('fullName')?.value.trim() || '';
        const email           = document.getElementById('email')?.value.trim() || '';
        const cellphone       = document.getElementById('cellphone')?.value.trim() || '';
        const identityNo      = document.getElementById('identityNo')?.value.trim() || '';
        const password        = document.getElementById('password')?.value || '';
        const confirmPassword = document.getElementById('confirmPassword')?.value || '';
        // const dob          = document.getElementById('dob')?.value || '';

        const select         = document.getElementById('privileges');
        const selectedOption = select.options[select.selectedIndex];
        const privilegesId   = selectedOption.value ? parseInt(selectedOption.value, 10) : NaN;
        const privilegeName  = selectedOption.dataset.name || selectedOption.textContent || '';

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

        const payload = {
            userFullName: fullName,
            userEmailAddress: email,
            userCellphoneNo: cellphone,
            userIdentityNo: identityNo,
            userPassword: password,
            confirmPassword: confirmPassword,
            userStatus: 1,
            privileges: {
                id: privilegesId,
                privilegeName: privilegeName
            }
            // dateOfBirth: dob,
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
                    window.location.href = '/login'; // adjust if needed
                }, 1800);
            } else {
                let message = `Registration failed (${response.status})`;
                try {
                    const errorData = await response.json();
                    if (Array.isArray(errorData) && errorData.length > 0) {
                        message = errorData[0].resolveIssueDetails || errorData[0].message || message;
                    } else if (errorData.resolveIssueDetails) {
                        message = errorData.resolveIssueDetails;
                    } else if (errorData.message) {
                        message = errorData.message;
                    } else if (errorData.detail) {
                        message = errorData.detail;
                    }
                } catch {
                    // parsing failed → keep default message
                }
                showAlert(message, 'danger');
            }
        } catch (err) {
            console.error('Registration error:', err);
            showAlert('Network error – please check your connection and try again.', 'danger');
        } finally {
            resetButton();
        }

        function resetButton() {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register';
        }
    });
});