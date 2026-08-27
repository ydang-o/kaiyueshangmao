import autoImport from 'unplugin-auto-import/vite'

export default function createAutoImport() {
    return (autoImport.default || autoImport)({
        imports: [
            'vue',
            'vue-router',
            'pinia'
        ],
        dts: false
    })
}
