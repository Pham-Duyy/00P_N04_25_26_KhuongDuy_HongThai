// main.js
        // Kiểm tra đăng nhập
        function checkAuth() {
            const isLoggedIn = localStorage.getItem('isLoggedIn');
            const user = localStorage.getItem('user');
            if (isLoggedIn !== 'true' || !user) {
                window.location.href = "../static/index.html";
                return false;
            }
            const userData = JSON.parse(user);
            document.getElementById('userName').textContent = userData.name || userData.email;
            document.getElementById('userRole').textContent = userData.role || 'Thành viên';
            return true;
        }

        // Sidebar toggle
        document.getElementById('sidebarToggle').addEventListener('click', function() {
            const sidebar = document.getElementById('sidebar');
            const overlay = document.getElementById('sidebarOverlay');
            sidebar.classList.toggle('-translate-x-full');
            overlay.classList.toggle('hidden');
        });

        document.getElementById('sidebarOverlay').addEventListener('click', function() {
            const sidebar = document.getElementById('sidebar');
            const overlay = document.getElementById('sidebarOverlay');
            sidebar.classList.add('-translate-x-full');
            overlay.classList.add('hidden');
        });

        // Navigation active state
        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', function(e) {
                e.preventDefault();
                document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active', 'bg-blue-50', 'text-blue-700'));
                this.classList.add('active', 'bg-blue-50', 'text-blue-700');
            });
        });

        // Quick action buttons
        document.querySelectorAll('.quick-action-btn').forEach((btn, index) => {
            btn.addEventListener('click', function() {
                showNotification('Tính năng đang phát triển', 'info');
            });
        });

        // Logout functionality
        document.getElementById('logoutBtn').addEventListener('click', function() {
            localStorage.removeItem('isLoggedIn');
            localStorage.removeItem('user');
            window.location.href = "../static/index.html";
        });

        // Notification system
        function showNotification(message, type = 'info') {
            const notification = document.getElementById('notification');
            const icon = document.getElementById('notificationIcon');
            const messageEl = document.getElementById('notificationMessage');
            let iconClass = '';
            switch(type) {
                case 'success': iconClass = 'fas fa-check-circle text-green-500'; break;
                case 'error': iconClass = 'fas fa-exclamation-circle text-red-500'; break;
                case 'warning': iconClass = 'fas fa-exclamation-triangle text-yellow-500'; break;
                default: iconClass = 'fas fa-info-circle text-blue-500';
            }
            icon.innerHTML = `<i class="${iconClass} text-xl"></i>`;
            messageEl.textContent = message;
            notification.classList.remove('hidden');
            setTimeout(() => { notification.classList.add('hidden'); }, 3000);
        }

        // Load user groups
        async function loadUserGroups() {
            try {
                const user = JSON.parse(localStorage.getItem('user'));
                const response = await fetch(`http://localhost:8080/groups/user/${user.id}`);
                const groups = await response.json();
                const selector = document.getElementById('groupSelector');
                selector.innerHTML = '<option value="">Chọn nhóm</option>';
                groups.forEach(group => {
                    const option = document.createElement('option');
                    option.value = group.id;
                    option.textContent = group.name;
                    selector.appendChild(option);
                });
            } catch (error) {
                console.error('Error loading groups:', error);
            }
        }

        // Load group data
        async function loadGroupData(groupId) {
            if (!groupId) return;
            try {
                const response = await fetch(`http://localhost:8080/groups/${groupId}/summary`);
                const data = await response.json();
                document.getElementById('totalBalance').textContent = `${data.totalBalance.toLocaleString()} VNĐ`;
                document.getElementById('monthlyIncome').textContent = `${data.monthlyIncome.toLocaleString()} VNĐ`;
                document.getElementById('monthlyExpense').textContent = `${data.monthlyExpense.toLocaleString()} VNĐ`;
                document.getElementById('groupMembers').textContent = data.memberCount;
            } catch (error) {
                console.error('Error loading group data:', error);
            }
        }

        document.getElementById('groupSelector').addEventListener('change', function() {
            loadGroupData(this.value);
        });

        document.addEventListener('DOMContentLoaded', function() {
            if (!checkAuth()) return;
            loadUserGroups();
        });