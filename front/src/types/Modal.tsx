export interface ModalProps {
  modalItems : {
    title?: string,
    content: string,
    modalType: 'txtOnly' | 'warn' | 'done' | 'input' | 'ing',
    btn: number
  }
}