import { h, defineComponent } from "vue";
import TypeIt from "typeit";

// 打字机效果组件
export default defineComponent ({
    name: "TypeIt",
    props: {
        /** 打字速度，以每一步之间的毫秒数为单位 */
        speed: {
            type: Number,
            default: 200
        },
        // 传入的参数
        values: {
            type: Array,
            defalut: []
        },
        // 类型
        className: {
            type: String,
            default: "type-it"
        },
        // 是否显示光标
        cursor: {
            type: Boolean,
            default: true
        }
    },
    render() {
        return h(
            "span",
            {
                class: this.className
            },
            {
                default: () => []
            }
        );
    },
    mounted() {
        new TypeIt(`.${this.className}`, {
            strings: this.values,
            speed: this.speed,
            cursor: this.cursor
        }).go();
    }
})