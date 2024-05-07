export interface ModalProps {
  onSubmitBtnClick: () => void,
  onCancelBtnClick: () => void,
  modalItems : {
    title?: string,
    content: string,
    modalType: 'txtOnly' | 'warn' | 'done' | 'input' | 'ing',
    btn: number
  }
}