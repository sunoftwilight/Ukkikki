export interface ModalProps {
  modalItems : {
    content: string,
    modalType: 'txtOnly' | 'warn' | 'done'
  }
}