<template>
  <a-config-provider :locale="locale">
    <router-view />
  </a-config-provider>
</template>

<script setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'

const locale = zhCN
</script>

<style>
/* ============================================
   Study Room Reservation - Light Theme
   White + Light Cyan Design System
   ============================================ */

/* Import Google Fonts */
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

/* CSS Variables - Light Theme */
:root {
  /* Background Colors - White based */
  --bg-primary: #f8fafc;
  --bg-secondary: #ffffff;
  --bg-card: #ffffff;
  --bg-elevated: #f1f5f9;
  --bg-hover: #f0f9ff;
  
  /* Text Colors */
  --text-primary: #0f172a;
  --text-secondary: #475569;
  --text-muted: #94a3b8;
  --text-disabled: #cbd5e1;
  
  /* Accent Colors - Light Cyan */
  --accent-primary: #06b6d4;
  --accent-secondary: #0891b2;
  --accent-light: #ecfeff;
  --accent-lighter: #cffafe;
  --accent-glow: rgba(6, 182, 212, 0.1);
  --accent-bg: linear-gradient(135deg, #ecfeff 0%, #cffafe 100%);
  
  /* Status Colors */
  --success: #10b981;
  --success-bg: #d1fae5;
  --success-light: #ecfdf5;
  --warning: #f59e0b;
  --warning-bg: #fef3c7;
  --warning-light: #fffbeb;
  --error: #ef4444;
  --error-bg: #fee2e2;
  --error-light: #fef2f2;
  --info: #3b82f6;
  --info-bg: #dbeafe;
  --info-light: #eff6ff;
  
  /* Border Colors */
  --border-primary: #e2e8f0;
  --border-subtle: #f1f5f9;
  --border-hover: #cbd5e1;
  
  /* Sidebar */
  --sidebar-bg: #ffffff;
  --sidebar-hover: #f0f9ff;
  --sidebar-active: linear-gradient(135deg, #ecfeff 0%, #cffafe 100%);
  
  /* Shadows */
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.07), 0 2px 4px -2px rgba(0, 0, 0, 0.05);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.08), 0 4px 6px -4px rgba(0, 0, 0, 0.05);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.05);
  
  /* Border Radius */
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;
  --radius-full: 9999px;
  
  /* Transitions */
  --transition-fast: 150ms cubic-bezier(0.4, 0, 0.2, 1);
  --transition-normal: 250ms cubic-bezier(0.4, 0, 0.2, 1);
  --transition-slow: 400ms cubic-bezier(0.4, 0, 0.2, 1);
}

/* Reset & Base Styles */
*, *::before, *::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  scroll-behavior: smooth;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background: var(--bg-primary);
  color: var(--text-primary);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
}

/* Scrollbar Styling */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: var(--bg-primary);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: var(--border-primary);
  border-radius: 3px;
  transition: background var(--transition-fast);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--border-hover);
}

/* Selection */
::selection {
  background: var(--accent-lighter);
  color: var(--accent-secondary);
}

/* Focus Styles */
:focus-visible {
  outline: 2px solid var(--accent-primary);
  outline-offset: 2px;
}

/* Card Base */
.card {
  background: var(--bg-card);
  border: 1px solid var(--border-primary);
  border-radius: var(--radius-lg);
  padding: 24px;
  transition: all var(--transition-normal);
  box-shadow: var(--shadow-sm);
}

.card:hover {
  border-color: var(--accent-primary);
  box-shadow: var(--shadow-md);
}

/* Button Base Styles */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 20px;
  font-weight: 500;
  font-size: 0.875rem;
  border-radius: var(--radius-md);
  border: none;
  cursor: pointer;
  transition: all var(--transition-normal);
}

.btn-primary {
  background: linear-gradient(135deg, var(--accent-primary) 0%, var(--accent-secondary) 100%);
  color: white;
  box-shadow: 0 4px 14px rgba(6, 182, 212, 0.25);
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(6, 182, 212, 0.35);
}

.btn-primary:active {
  transform: translateY(0);
}

/* Badge Styles */
.badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 500;
  border-radius: var(--radius-full);
}

.badge-success {
  background: var(--success-bg);
  color: var(--success);
}

.badge-warning {
  background: var(--warning-bg);
  color: var(--warning);
}

.badge-error {
  background: var(--error-bg);
  color: var(--error);
}

.badge-info {
  background: var(--info-bg);
  color: var(--info);
}

/* Ant Design Overrides - Light Theme */
.ant-input,
.ant-input-password,
.ant-select-selector,
.ant-picker {
  background: var(--bg-secondary) !important;
  border-color: var(--border-primary) !important;
  color: var(--text-primary) !important;
}

.ant-input:focus,
.ant-input:hover,
.ant-input-affix-wrapper:focus,
.ant-input-affix-wrapper:hover {
  border-color: var(--accent-primary) !important;
}

.ant-input:focus,
.ant-input-focused {
  box-shadow: 0 0 0 3px var(--accent-glow) !important;
}

.ant-input::placeholder {
  color: var(--text-muted) !important;
}

/* Button Primary Override */
.ant-btn-primary {
  background: linear-gradient(135deg, var(--accent-primary) 0%, var(--accent-secondary) 100%) !important;
  border: none !important;
  box-shadow: 0 4px 14px rgba(6, 182, 212, 0.2) !important;
}

.ant-btn-primary:hover {
  background: linear-gradient(135deg, #22d3ee 0%, var(--accent-primary) 100%) !important;
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(6, 182, 212, 0.3) !important;
}

.ant-btn-primary:active {
  transform: translateY(0);
}

/* Table Overrides */
.ant-table {
  background: transparent !important;
}

.ant-table-thead > tr > th {
  background: var(--bg-elevated) !important;
  color: var(--text-secondary) !important;
  border-bottom: 1px solid var(--border-primary) !important;
  font-weight: 600;
}

.ant-table-tbody > tr > td {
  background: var(--bg-card) !important;
  border-bottom: 1px solid var(--border-subtle) !important;
  color: var(--text-primary);
}

.ant-table-tbody > tr:hover > td {
  background: var(--bg-hover) !important;
}

/* Card Overrides */
.ant-card {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-primary) !important;
  border-radius: var(--radius-lg) !important;
  box-shadow: var(--shadow-sm) !important;
}

.ant-card-head {
  background: var(--bg-elevated) !important;
  border-bottom: 1px solid var(--border-primary) !important;
  color: var(--text-primary) !important;
}

.ant-card-body {
  color: var(--text-primary);
}

/* Modal Overrides */
.ant-modal-content {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-primary);
  box-shadow: var(--shadow-xl) !important;
}

.ant-modal-header {
  background: var(--bg-card) !important;
  border-bottom: 1px solid var(--border-primary) !important;
}

.ant-modal-title {
  color: var(--text-primary) !important;
}

.ant-modal-close-x {
  color: var(--text-muted);
}

.ant-modal-close-x:hover {
  color: var(--text-primary);
}

.ant-modal-footer {
  border-top: 1px solid var(--border-primary) !important;
}

/* Select/Dropdown Overrides */
.ant-select-selector {
  background: var(--bg-secondary) !important;
  border-color: var(--border-primary) !important;
  color: var(--text-primary) !important;
}

.ant-select:hover .ant-select-selector {
  border-color: var(--accent-primary) !important;
}

.ant-select-dropdown {
  background: var(--bg-card) !important;
  border: 1px solid var(--border-primary);
  box-shadow: var(--shadow-lg) !important;
}

.ant-select-item {
  color: var(--text-primary) !important;
}

.ant-select-item-option-active {
  background: var(--bg-hover) !important;
}

.ant-select-item-option-selected {
  background: var(--accent-light) !important;
  color: var(--accent-secondary) !important;
}

/* Pagination */
.ant-pagination-item {
  background: var(--bg-secondary) !important;
  border-color: var(--border-primary) !important;
}

.ant-pagination-item a {
  color: var(--text-primary) !important;
}

.ant-pagination-item-active {
  border-color: var(--accent-primary) !important;
  background: var(--accent-light) !important;
}

.ant-pagination-item-active a {
  color: var(--accent-secondary) !important;
}

/* Tag Overrides */
.ant-tag {
  border-radius: var(--radius-full) !important;
  border: none !important;
}

/* Tooltip */
.ant-tooltip-inner {
  background: var(--text-primary) !important;
  color: var(--bg-card) !important;
}

/* Message */
.ant-message-notice-content {
  background: var(--bg-card) !important;
  color: var(--text-primary) !important;
  border: 1px solid var(--border-primary);
  box-shadow: var(--shadow-lg) !important;
}

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { 
    opacity: 0; 
    transform: translateY(20px); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

@keyframes slideIn {
  from { 
    opacity: 0; 
    transform: translateX(-20px); 
  }
  to { 
    opacity: 1; 
    transform: translateX(0); 
  }
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.6; }
}

.animate-fade-in {
  animation: fadeIn var(--transition-normal) ease-out;
}

.animate-slide-up {
  animation: slideUp var(--transition-slow) ease-out;
}

.animate-slide-in {
  animation: slideIn var(--transition-slow) ease-out;
}

/* Responsive */
@media (max-width: 768px) {
  .hide-mobile {
    display: none !important;
  }
}

@media (min-width: 769px) {
  .hide-desktop {
    display: none !important;
  }
}

/* Reduced Motion */
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}
</style>
